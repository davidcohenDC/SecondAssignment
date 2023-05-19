package sourceanalysis;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Report {

    private final ConcurrentMap<Integer, List<Path>> reportData;

    public Report(Map<Integer, List<Path>> reportData) {
        if(reportData == null) {
            throw new IllegalArgumentException("Report data cannot be null");
        }
        this.reportData = new ConcurrentHashMap<>(reportData);
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
}
