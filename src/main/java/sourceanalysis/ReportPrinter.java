package sourceanalysis;

import java.nio.file.Path;
import java.util.List;
public class ReportPrinter {

    public static String getDistributionString(Report report, int numIntervals, int maxLines) {
        int intervalSize = maxLines / numIntervals;
        int remainingLines = maxLines % numIntervals;
        StringBuilder sb = new StringBuilder();

        int lowerBound = 0;
        int upperBound = intervalSize;
        for (int i = 0; i < numIntervals; i++) {
            if (i < remainingLines) {
                upperBound += 1;
            }
            sb.append("[").append(lowerBound).append(", ").append(upperBound).append("]: ").append(report.getFilesCount(i)).append("\n");
            lowerBound = upperBound + 1;
            upperBound += intervalSize;
        }
        sb.append("[").append(lowerBound).append(", inf]: ").append(report.getFilesCount(numIntervals)).append("\n");

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
