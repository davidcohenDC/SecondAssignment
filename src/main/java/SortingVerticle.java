import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.util.Comparator;
import java.util.TreeSet;

public class SortingVerticle extends AbstractVerticle {
    private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    private final int[] buckets;
    private final int maxTopFiles;
    private final int bucketSize;

    public SortingVerticle(int nBuckets, int maxLines, int maxTopFiles) {
        this.buckets = new int[nBuckets];
        this.maxTopFiles = maxTopFiles;
        this.bucketSize = maxLines / (nBuckets - 1);
    }

    public void start() {
        EventBus eb = this.getVertx().eventBus();
        eb.consumer("name-and-lines", entry -> {
            FileEntry fileEntry = (FileEntry) entry.body();
            this.files.add(fileEntry);
            if (this.files.size() > this.maxTopFiles)
                this.files.remove(this.files.last());
            int bucketIdx = (int)Math.min(((FileEntry) entry.body()).lines() / this.bucketSize, this.buckets.length-1);
            this.buckets[bucketIdx]++;
        });
    }
}
