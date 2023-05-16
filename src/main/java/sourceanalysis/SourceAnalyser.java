package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import java.nio.file.Path;

public interface SourceAnalyser {

    /**
     * Analyze the sources in the given directory
     * @param directory the directory to analyze
     * @return the report of the analysis
     */
    Single<Report> getReport(Path directory);

    /**
     * Analyze the sources in the given directory with incremental analysis of
     * results and is stoppable
     * @param directory the directory to analyze
     * @return the report of the analysis
     */
    Flowable<Report> analyzeSources(Path directory);

}
