package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.IOException;
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
                .flatMap(path -> processFile(path, numIntervals, maxLines))
                .scan(Report::mergeReports);
    }
    private Flowable<Report> processFile(Path path, int numIntervals, int maxLines) {
        try (Stream<String> lines = Files.lines(path)) {
            long lineCount = lines
                    .filter(line -> !line.trim().isEmpty())  // ignore empty lines
                    .filter(line -> !line.trim().startsWith("//"))  // ignore single-line comments
                    .filter(line -> !line.trim().startsWith("/*"))  // ignore single-line comments
                    .filter(line -> !line.trim().endsWith("/*"))  // ignore single-line comments
                    .count();  // count the remaining lines

            int interval = numIntervals;
            if (lineCount < maxLines) {
                interval = (int) lineCount / (maxLines / numIntervals);
            }

            System.out.println("| Interval: " + interval);

            Map<Integer, List<Path>> reportData = new HashMap<>();
            reportData.computeIfAbsent(interval, k -> new ArrayList<>()).add(path);

            Report report = new Report(reportData);
            // Stampa l'informazione richiesta in un altro thread
            Flowable.just(report)
                    .observeOn(Schedulers.io())
                    .subscribe(r -> r.getReportData().forEach((k, v) -> {
                        System.out.println("Interval: " + k + "| Files: " + v.size());
                    }));

            return Flowable.just(report);
        } catch (IOException e) {
            return Flowable.error(e);
        }
    }

}
