package sourceanalysis;

import io.reactivex.rxjava3.core.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class PathCrawler {
    public Flowable<Path> crawlDirectory(Path directory) {
        return Flowable.create(emitter -> crawl(directory, emitter), BackpressureStrategy.BUFFER);
    }

    private void crawl(Path directory, FlowableEmitter<Path> emitter) {
        try (Stream<Path> fileStream = Files.walk(directory)) {
            fileStream
                    .parallel()
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".java"))
                    .forEach(file -> {
                        try {
                            emitter.onNext(file);
                        } catch (Exception e) {
                            emitter.tryOnError(e);
                        }
                    });
            if (!emitter.isCancelled()) {
                emitter.onComplete();
            }
        } catch (Exception e) {
            emitter.onError(e);
        }
    }
}
