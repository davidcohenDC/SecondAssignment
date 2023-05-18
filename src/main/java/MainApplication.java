import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.util.List;
import static java.lang.Thread.sleep;

public class MainApplication {

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("message", System.getProperty("user.home") + "/Desktop"));
        Handler<AsyncResult<String>> activation;
        vertx.deployVerticle(new WolkerVerticle(new File("C:\\Users\\mikim")), options, res -> {
            vertx.deployVerticle(new CountingVerticle(), res2 -> {
                vertx.deployVerticle(new SortingVerticle(5, 1000, 10), options);
            });
        });
    }
}
