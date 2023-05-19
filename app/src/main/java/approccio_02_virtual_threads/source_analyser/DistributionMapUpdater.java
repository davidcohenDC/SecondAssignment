package approccio_02_virtual_threads.source_analyser;

import approccio_02_virtual_threads.boundedbuffer.Distribution;

import java.nio.file.Path;

public class DistributionMapUpdater {
    private final Distribution<Integer, Path> distribution;

    public DistributionMapUpdater(Distribution<Integer, Path> distribution) {
        this.distribution = distribution;
    }

    public void processFile(int interval, Path file) {
        try {
            synchronized (distribution) {
                distribution.writeInterval(interval, file);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
