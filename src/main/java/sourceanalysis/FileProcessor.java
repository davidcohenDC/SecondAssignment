package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileProcessor {
    public Flowable<Report> processFiles(Flowable<Path> pathFlowable, int numIntervals, int maxLines) {
        return pathFlowable
                .subscribeOn(Schedulers.computation())
                .flatMap(path -> processFile(path, numIntervals, maxLines),10)
//                .subscribeOn(Schedulers.computation())
                .scan(Report::mergeReports);
    }

    private Flowable<Report> processFile(Path path, int numIntervals, int maxLines) {
        return Flowable.using(
                () -> Files.lines(path),
                linesStream -> {
                    Flowable<String> lines = Flowable.fromIterable(linesStream::iterator);
                    return lines
                            .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("//"))  // ignore empty lines
                            .filter(line -> !line.matches(".*\\*\\/.*"))  // ignore inline comments
                            .count()  // count the remaining lines
                            .toFlowable()
                            .map(lineCount -> {
                                int interval = numIntervals;
                                if (lineCount < maxLines) {
                                    interval = (int) (long) lineCount / (maxLines / numIntervals);
                                }

                                Map<Integer, List<Path>> reportData = new HashMap<>();
                                reportData.computeIfAbsent(interval, k -> new ArrayList<>()).add(path);

                                return new Report(reportData);
                            });
                },
                Stream::close
        ).subscribeOn(Schedulers.io());
    }

}
