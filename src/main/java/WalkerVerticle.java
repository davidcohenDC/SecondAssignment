import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import java.io.File;

public class WalkerVerticle extends AbstractVerticle {

    public void start() {
        EventBus eventBus = this.vertx.eventBus();
        eventBus.consumer("file-to-explore", next -> {
            File dir = new File(next.body().toString());
            if (dir.isDirectory()){
                this.vertx.fileSystem().readDir(dir.getAbsolutePath(), newFile -> {
                    if (newFile.succeeded()) {
                        for (var f : newFile.result()) {
                            eventBus.publish("file-to-explore", f);
                        }
                    }
                });
            } else {
                if (dir.getName().endsWith(".java")){
                    eventBus.publish("file-to-count", dir);
                }
            }
        });
        eventBus.consumer("all-done", msg -> {
           vertx.close();
        });
    }
}
