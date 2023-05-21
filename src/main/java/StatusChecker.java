import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class StatusChecker extends AbstractVerticle {

    private int counter;

    public void start() {
        EventBus eventBus = this.vertx.eventBus();
        eventBus.consumer("file-to-count", message -> {
            this.counter = this.counter + 1;
        });
        eventBus.consumer("counted", message -> {
            this.counter = this.counter - 1;
        });
        if (this.counter == 0) {
            eventBus.publish("all-done", 1);
        }
        eventBus.consumer("all-done", msg -> {
           vertx.close();
        });
    }
}
