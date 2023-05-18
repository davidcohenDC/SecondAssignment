import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.OpenOptions;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CountingVerticle extends AbstractVerticle {
    public void start() {
        System.out.println("Counter activated");
        EventBus eb = this.getVertx().eventBus();
        eb.consumer("file-to-count", message -> {
            File file = (File) message.body();
            vertx.fileSystem().open(file.getName(), new OpenOptions(), result -> {
                if (result.succeeded()) {
                    if (file.getName().endsWith(".java")){
                        try (var lines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
                            System.out.println("Counting...");
                            FileEntry nextEntry = new FileEntry(file.getPath(), lines.count());
                            eb.publish("name-and-lines", nextEntry);
                        } catch (Exception ignore) {
                        }
                    }
                } else {
                    System.err.println("Failed to open the file: " + result.cause().getMessage());
                }
            });
        });
    }
}

