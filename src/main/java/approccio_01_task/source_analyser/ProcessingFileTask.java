package approccio_01_task.source_analyser;

import approccio_01_task.utils.FileUtils;

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
            int interval = this.params.getInterval(FileUtils.countLines(path), path);
            this.updater.processFile(interval, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
