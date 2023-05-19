package approccio_02_virtual_threads.source_analyser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface SourceAnalyser {

    Future<Report> getReport();
    CompletableFuture<Boolean> analyzeSources();
}
