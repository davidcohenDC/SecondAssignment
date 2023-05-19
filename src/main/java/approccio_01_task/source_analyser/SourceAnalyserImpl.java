package approccio_01_task.source_analyser;

import approccio_01_task.utils.PerformanceUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SourceAnalyserImpl implements SourceAnalyser {
    private final DirectoryWalkerParams params;
    private final int poolSize;
    public SourceAnalyserImpl(DirectoryWalkerParams params) {
        this.params = params;
        this.poolSize = PerformanceUtils.getDefaultNumThread();
    }
    public Future<Report> getReport() {
        DirectoryWalkerMaster walker = new DirectoryWalkerMaster(this.params, this.poolSize);
        walker.walk();

        ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
        return singleExecutor.submit(new PrinterTask(this.params));
    }
    public CompletableFuture<Boolean> analyzeSources() {
        DirectoryWalkerMaster walker = new DirectoryWalkerMaster(this.params, this.poolSize);
        return CompletableFuture.supplyAsync(walker::walk);
    }
}
