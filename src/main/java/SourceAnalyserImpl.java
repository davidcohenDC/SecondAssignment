import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class SourceAnalyserImpl implements SourceAnalyser {

    private final String directory;
    private final int maxFiles;
    private final int numIntervals;
    private final int maxLines;
    private Vertx vertx;
    private EventBus eventBus;

    public SourceAnalyserImpl(String directory, int maxFiles, int numIntervals, int maxLines) {
        this.directory = directory;
        this.maxFiles = maxFiles;
        this.numIntervals = numIntervals;
        this.maxLines = maxLines;
    }

    private Vertx analyser(String directory, int maxFiles, int numIntervals, int maxLines) {
        this.vertx = Vertx.vertx();
        this.eventBus = this.vertx.eventBus();
        vertx.deployVerticle(new StatusChecker(), done -> {
            vertx.deployVerticle(new ComputingVerticle(numIntervals, maxLines, maxFiles), onFinish -> {
                vertx.deployVerticle(new WalkerVerticle(), onDone -> {
                    eventBus.publish("file-to-explore", new File(directory));
                });
            });
        });
        return this.vertx;
    }

    @Override
    public void getReport() {
        this.analyser(this.directory, this.maxFiles, this.numIntervals, this.maxLines);
        eventBus.consumer("all-done", done -> {
            eventBus.consumer("distributions", results -> {
                final var distribution = (HashMap<Integer, Integer>) results.body();
                distribution.toString().replace("{", " ").replace("}", "").replace(",", "\n");
                System.out.println(distribution);
            });
            eventBus.consumer("topFiles", result -> {
                final var topFiles = result.body();
                System.out.println(topFiles);
            });
        });
    }

    @Override
    public CompletableFuture<Vertx> analyzeSources() {
        return CompletableFuture.supplyAsync(() -> this.analyser(directory, maxFiles, numIntervals, maxLines));
    }

}
