import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*public class Report {

    private final TreeSet<FileEntry> reportData;

    public Report(TreeSet<FileEntry> reportData) {
        if(reportData == null) {
            throw new IllegalArgumentException("Report data cannot be null");
        }
        this.reportData = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    }

    public static Report mergeReports(Report report1, Report report2) {
        Map<Integer, List<Path>> mergedData = new HashMap<>(report1.getReportData());
        report2.getReportData().forEach((key, value) ->
                mergedData.merge(key, new ArrayList<>(value), (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }));
        return new Report(mergedData);
    }

    public Map<Integer, List<Path>> getReportData() {
        return Collections.unmodifiableMap(reportData);
    }
}*/
