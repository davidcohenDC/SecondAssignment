package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import java.nio.file.Path;

public class SourceAnalyserImpl implements SourceAnalyser{

    /**
     * Analyze the sources in the given directory
     * @param directory the directory to analyze
     * @return the report of the analysis
     */
    @Override
    public Single<Report> getReport(Path directory) {
        Flowable<Path> pathFlowable = new PathCrawler().crawlDirectory(directory);
        return new FileProcessor().processFiles(pathFlowable).lastOrError();
    }

    /**
     * Analyze the sources in the given directory with incremental analysis of
     * results and is stoppable
     * @param directory the directory to analyze
     * @return the report of the analysis
     */
    @Override
    public Flowable<Report> analyzeSources(Path directory) {
        Flowable<Path> pathFlowable = new PathCrawler().crawlDirectory(directory);
        return new FileProcessor().processFiles(pathFlowable);
    }
}
