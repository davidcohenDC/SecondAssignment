package sourceanalysis;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ReportPrinter {

    public static String getDistributionString(Report report) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<Path>> entry : report.getReportData().entrySet()) {
            int interval = entry.getKey();
            List<Path> files = entry.getValue();
            sb.append("Interval: ").append(interval).append(", Files: ").append(files.size()).append("\n");
        }
        return sb.toString();
    }

    public static String getMaxFilesString(Report report, int maxFiles) {
        StringBuilder sb = new StringBuilder();
        report.getReportData().values().stream()
                .flatMap(List::stream)
                .limit(maxFiles)
                .map(Path::toString) // Convert Path objects to String
                .forEach(fileName -> sb.append(fileName).append("\n"));
        return sb.toString();
    }

}
