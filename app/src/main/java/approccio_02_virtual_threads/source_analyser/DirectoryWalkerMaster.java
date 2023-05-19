package approccio_02_virtual_threads.source_analyser;

import approccio_02_virtual_threads.utils.WalkerUtils;

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
    public DirectoryWalkerMaster(DirectoryWalkerParams params) {
        super(params);
        this.params = params;
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }
    @Override
    protected void walkRec(Path directory) throws IOException {
        if (!this.isRunning.get()) return;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                try {
                    if (WalkerUtils.isRegularJavaFile(path)) {

                        ProcessingFileTask processingFileTask = new ProcessingFileTask(this.params, path);
                        this.executorService.execute(processingFileTask);

                    } else if (WalkerUtils.isDirectoryNotHidden(path)) {
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
