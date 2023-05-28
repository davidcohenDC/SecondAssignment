import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.util.concurrent.CompletableFuture;

public interface SourceAnalyser {

    void getReport();
    CompletableFuture<Vertx> analyzeSources();
}
