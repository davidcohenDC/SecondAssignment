package approccio_01_task.source_analyser;

import approccio_01_task.utils.FileUtils;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DirectoryWalkerMaster extends AbstractDirectoryWalker {
    private final ExecutorService executorService;
    final DirectoryWalkerParams params;
    public DirectoryWalkerMaster(DirectoryWalkerParams params, int poolSize) {
        super(params);
        this.params = params;
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }
    @Override
    protected void walkRec(Path directory) throws IOException {
        if (!this.isRunning.get()) return;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                try {
                    if (FileUtils.isRegularJavaFile(path)) {

                        ProcessingFileTask processingFileTask = new ProcessingFileTask(this.params, path);
                        this.executorService.execute(processingFileTask);

                    } else if (FileUtils.isDirectoryNotHidden(path)) {
                        this.walkRec(path);
                    }
                } catch (AccessDeniedException e) {
                    System.out.println("Access denied to " + path);
                }
            }
        }
    }
    @Override
    protected void stopBehaviour() {
        this.executorService.shutdown();
    }
}
