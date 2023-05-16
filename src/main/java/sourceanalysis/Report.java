package sourceanalysis;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {

    private final Map<Integer, List<Path>> reportData;

    public Report(Map<Integer, List<Path>> reportData) {
        this.reportData = reportData;
    }

    public static Report mergeReports(Report report1, Report report2) {
        Map<Integer, List<Path>> mergedData = new HashMap<>(report1.getReportData());
        report2.getReportData().forEach((key, value) ->
                mergedData.merge(key, value, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }));
        return new Report(mergedData);
    }

    public Map<Integer, List<Path>> getReportData() {
        return reportData;
    }

}