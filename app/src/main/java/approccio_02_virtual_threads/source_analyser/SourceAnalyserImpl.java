package approccio_02_virtual_threads.source_analyser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SourceAnalyserImpl implements SourceAnalyser {
    private final DirectoryWalkerParams params;
    public SourceAnalyserImpl(DirectoryWalkerParams params) {
        this.params = params;
    }
    public Future<Report> getReport() {
        DirectoryWalkerMaster walker = new DirectoryWalkerMaster(this.params);
        walker.walk();

        ExecutorService singleExecutor = Executors.newVirtualThreadPerTaskExecutor();
        return singleExecutor.submit(new PrinterTask(this.params));
    }
    public CompletableFuture<Boolean> analyzeSources() {
        DirectoryWalkerMaster walker = new DirectoryWalkerMaster(this.params);
        return CompletableFuture.supplyAsync(walker::walk);
    }
}
