package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileProcessor {
    public Flowable<Report> processFiles(Flowable<Path> pathFlowable) {
        return pathFlowable
                .flatMap(this::processFile)
                .scan(Report::mergeReports);
    }
    private Flowable<Report> processFile(Path path) {
        try {
            Integer lineCount = Files.readAllLines(path).size(); // Metodo per il conteggio delle righe nel file
            Map<Integer, List<Path>> reportData = new HashMap<>();
            reportData.computeIfAbsent(lineCount, k -> new ArrayList<>()).add(path);
            Report report = new Report(reportData);
            return Flowable.just(report);
        } catch (IOException e) {
            return Flowable.error(e);
        }
    }
}
