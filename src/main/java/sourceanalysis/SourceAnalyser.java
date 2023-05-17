package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface SourceAnalyser {

    /**
     * Analyze the sources in the given directory
     * @return the report of the analysis
     */
    Single<Report> getReport();

    /**
     * Analyze the sources in the given directory with incremental analysis of
     * results and is stoppable
     * @return the report of the analysis
     */
    Flowable<Report> analyzeSources();

}
