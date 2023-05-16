package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import java.nio.file.Path;

public class SourceAnalyserImpl implements SourceAnalyser {

    private final PathCrawler pathCrawler;
    private final FileProcessor fileProcessor;

    private final int numIntervals;
    private final int maxLines;



    public SourceAnalyserImpl(PathCrawler pathCrawler, FileProcessor fileProcessor, int numIntervals, int maxLines) {
        this.pathCrawler = pathCrawler;
        this.fileProcessor = fileProcessor;
        this.numIntervals = numIntervals;
        this.maxLines = maxLines;
    }

    /**
     * Analyze the sources in the given directory
     * @param directory the directory to analyze
     * @return the report of the analysis
     */
    @Override
    public Single<Report> getReport(Path directory) {
        Flowable<Path> pathFlowable = pathCrawler.crawlDirectory(directory);
        return fileProcessor.processFiles(pathFlowable, numIntervals, maxLines).lastOrError();
    }

    /**
     * Analyze the sources in the given directory with incremental analysis of
     * results and is stoppable
     * @param directory the directory to analyze
     * @return the report of the analysis
     */
    @Override
    public Flowable<Report> analyzeSources(Path directory) {
        Flowable<Path> pathFlowable = pathCrawler.crawlDirectory(directory);
        return fileProcessor.processFiles(pathFlowable, numIntervals, maxLines);
    }
}
