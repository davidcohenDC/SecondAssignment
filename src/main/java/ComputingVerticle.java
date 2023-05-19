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
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public class ComputingVerticle extends AbstractVerticle {
    private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    private final int[] buckets;
    private final int maxTopFiles;
    private final int bucketSize;

    public ComputingVerticle(int nBuckets, int maxLines, int maxTopFiles) {
        this.buckets = new int[nBuckets];
        this.maxTopFiles = maxTopFiles;
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
                System.out.println(this.files + " " + this.files.size());
                if (this.files.size() > this.maxTopFiles) {
                    this.files.remove(this.files.last());
                }
                int bucketIdx = (int)Math.min(r.result() / this.bucketSize, this.buckets.length-1);
                this.buckets[bucketIdx]++;
                eb.publish("counted", 1);
            });
        });
    }
}
