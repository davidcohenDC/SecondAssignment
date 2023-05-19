package approccio_02_virtual_threads.source_analyser;

import approccio_02_virtual_threads.utils.WalkerUtils;

import java.io.IOException;
import java.nio.file.Path;

public class ProcessingFileTask implements Runnable {
    private final DistributionMapUpdater updater;
    private final DirectoryWalkerParams params;
    private final Path path;

    public ProcessingFileTask(DirectoryWalkerParams params, Path path) {
        this.updater = new DistributionMapUpdater(params.getDistribution());
        this.params = params;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            int interval = this.params.getInterval(WalkerUtils.countLines(path), path);
            this.updater.processFile(interval, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
