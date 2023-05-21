import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class AnalyserCLI {

    public AnalyserCLI(String dir, int numIntervals, int maxLength, int maxFiles) {
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus();
        vertx.deployVerticle(new StatusChecker(), done -> {
            vertx.deployVerticle(new ComputingVerticle(numIntervals, maxLength, maxFiles), onFinish -> {
                vertx.deployVerticle(new WalkerVerticle(), onDone -> {
                    eventBus.publish("file-to-explore", new File(dir));
                });
            });
        });
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
}