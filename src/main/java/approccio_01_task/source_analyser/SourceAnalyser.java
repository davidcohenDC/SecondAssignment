package approccio_01_task.source_analyser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface SourceAnalyser {

    Future<Report> getReport();
    CompletableFuture<Boolean> analyzeSources();
}
