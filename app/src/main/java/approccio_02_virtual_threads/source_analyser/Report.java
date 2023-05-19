package approccio_02_virtual_threads.source_analyser;

import approccio_02_virtual_threads.utils.Pair;

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
