import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.OpenOptions;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComputingVerticle extends AbstractVerticle {
    private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    private final Map<Integer, Integer> buckets = new HashMap<>();
    private final int maxTopFiles;
    private final int bucketSize;
    private final int nBuckets;
    private final int maxLines;

    public ComputingVerticle(int nBuckets, int maxLines, int maxTopFiles) {
        this.maxTopFiles = maxTopFiles;
        this.nBuckets = nBuckets;
        this.maxLines = maxLines;
        this.bucketSize = maxLines / (nBuckets - 1);
    }
    public void start() {
        EventBus eb = this.getVertx().eventBus();
        eb.consumer("file-to-count", message -> {
            Future<Long> results = this.getVertx().executeBlocking(counting -> {
                File file = (File) message.body();
                try (var lines = Files.lines(Path.of(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
                    final long countedLines = lines.count();
                    counting.complete(countedLines);
                } catch (Exception ignore) {
                }
            });
            results.onComplete((AsyncResult<Long> r) -> {
                FileEntry fileEntry = new FileEntry(((File) message.body()).getPath(), r.result());
                this.files.add(fileEntry);
                if (this.files.size() > this.maxTopFiles) {
                    this.files.remove(this.files.last());
                }
                int bucketKey = (int)Math.min(((int)(r.result() / this.bucketSize)*this.bucketSize) , this.maxLines);
                buckets.merge(bucketKey, 1, Integer::sum);
                eb.publish("counted", 1);
                eb.publish("distributions", this.buckets);
                eb.publish("topFiles", files.toString().replace("FileEntry", "\n").replace("[", "").replace("]", ""));
            });
        });
        eb.consumer("all-done", msg -> {
            vertx.close();
        });
    }

    /*public List<FileEntry> getTopFiles() {
        return this.files.stream().toList();
    }

    public Map<Integer, Integer> getBuckets() {
        return IntStream.range(0, this.buckets.length).boxed()
                .collect(Collectors.toMap(i -> i * this.bucketSize, i -> this.buckets[i]));
    }*/
}
