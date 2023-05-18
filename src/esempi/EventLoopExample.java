import io.vertx.core.Vertx;

public class EventLoopExample {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.setPeriodic(1000, timerId -> {
            System.out.println("This task runs on the event loop");
        });

        vertx.executeBlocking(promise -> {
            try {
                Thread.sleep(3000);
                promise.complete();
            } catch (InterruptedException e) {
                promise.fail("Blocking operation was interrupted");
            }
        }, result -> {
            if (result.succeeded()) {
                System.out.println("This task runs on a worker thread");
            } else {
                System.err.println("Failed to execute blocking operation: " + result.cause());
            }
        });

        System.out.println("This code runs on the main thread");

        try {
            Thread.sleep(5000); // Add a delay of 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vertx.close();
    }
}

import io.vertx.core.AbstractVerticle;
        import io.vertx.core.eventbus.EventBus;
        import io.vertx.core.file.AsyncFile;
        import io.vertx.core.file.OpenOptions;
        import io.vertx.core.streams.Pump;

public class ProducerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();

        // Simulate file production
        String filePath = "path/to/file.txt";
        String fileContent = "Hello, Event Bus!";

        // Pass file path to the consumer verticle
        eventBus.publish("file.path", filePath);

        // Pass file content to the consumer verticle
        eventBus.publish("file.content", fileContent);
    }
}

public class ConsumerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();

        // Listen for file path
        eventBus.consumer("file.path", message -> {
            String filePath = (String) message.body();

            // Read the file
            vertx.fileSystem().open(filePath, new OpenOptions(), result -> {
                if (result.succeeded()) {
                    AsyncFile asyncFile = result.result();
                    Pump pump = Pump.pump(asyncFile, vertx.createHttpClient().request(
                            HttpMethod.POST,
                            8080,
                            "localhost",
                            "/destination"
                    ));
                    pump.start();
                } else {
                    System.err.println("Failed to open the file: " + result.cause().getMessage());
                }
            });
        });

        // Listen for file content
        eventBus.consumer("file.content", message -> {
            String fileContent = (String) message.body();
            // Perform operations on the file content
            System.out.println("Received file content: " + fileContent);
        });
    }
}
