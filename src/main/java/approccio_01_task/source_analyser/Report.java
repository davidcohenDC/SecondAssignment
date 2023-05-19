package approccio_01_task.source_analyser;

import approccio_01_task.utils.Pair;

public class Report {

    private final Pair<String, String> report;

    public Report(Pair<String, String> report) {
        this.report = report;
    }

    public String getDistribution() {
        return this.report.getX();
    }

    public String getMaxFiles() {
        return this.report.getY();
    }
}
