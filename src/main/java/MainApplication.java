import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.util.List;
import static java.lang.Thread.sleep;

public class MainApplication {

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions();
        vertx.deployVerticle(new ComputingVerticle(5, 1000, 10), onFinish -> {
            vertx.deployVerticle(new WolkerVerticle(), onDone -> {
                EventBus eventBus = vertx.eventBus();
                eventBus.publish("file-to-explore", new File("C:\\Users\\mikim\\Desktop\\PCD\\SecondAssignment\\src\\main\\java"));
            });
        });
    }
}