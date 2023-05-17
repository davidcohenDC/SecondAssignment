package sourceanalysis;

import io.reactivex.rxjava3.core.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class PathCrawler {
    public Flowable<Path> crawlDirectory(Path directory) {
        return Flowable.create(emitter -> {
            try {
                crawl(directory, emitter);
            } catch (Exception e) {
                emitter.tryOnError(e);
            } finally {
                if (!emitter.isCancelled()) {
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    private void crawl(Path directory, FlowableEmitter<Path> emitter) throws Exception {
        try (Stream<Path> fileStream = Files.walk(directory)) {
            fileStream
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".java"))
                    .forEach(file -> {
                        if (!emitter.isCancelled()) {
                            emitter.onNext(file);
                        }
                    });
        }
    }
}
