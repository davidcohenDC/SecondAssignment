import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;

import java.io.File;

public class WolkerVerticle extends AbstractVerticle {

    private final File dir;

    public WolkerVerticle(File path) {
        this.dir = path;
    }

    public void start() {
        EventBus eventBus = vertx.eventBus();
        if (this.dir.isDirectory()){
            vertx.fileSystem().open(this.dir.getName(), new OpenOptions(), result -> {
                if (result.succeeded()) {
                    vertx.fileSystem().readDir(this.dir.getPath(), newFile -> {
                        if (newFile.succeeded()) {
                            for (var f : newFile.result()) {
                                vertx.deployVerticle(new WolkerVerticle(new File(f)));
                            }
                        }
                    });
                }
            });
        } else {
            eventBus.publish("file-to-count", dir);
        }
    }
}
