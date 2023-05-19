package sourceanalysis;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import java.nio.file.Path;
import java.util.HashMap;

public class SourceAnalyserImpl implements SourceAnalyser {
    private final PathCrawler pathCrawler;
    private final FileProcessor fileProcessor;
    private final int numIntervals;
    private final int maxLines;
    private final Path directory;

    public SourceAnalyserImpl(PathCrawler pathCrawler, FileProcessor fileProcessor, int numIntervals, int maxLines, Path directory) {
        if (pathCrawler == null || fileProcessor == null || directory == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        this.pathCrawler = pathCrawler;
        this.fileProcessor = fileProcessor;
        this.numIntervals = numIntervals;
        this.maxLines = maxLines;
        this.directory = directory;
    }

    /**
     * Analyze the sources in the given directory
     * @return the report of the analysis
     */
    @Override
    public Single<Report> getReport() {
        return analyzeAndReport()
                .lastOrError()
                .onErrorReturn(this::onError);
    }

    /**
     * Analyze the sources in the given directory with incremental analysis of
     * results and is stoppable
     * @return the report of the analysis
     */
    @Override
    public Flowable<Report> analyzeSources() {
        return analyzeAndReport()
                .onErrorReturn(this::onError);
    }


    private Flowable<Report> analyzeAndReport() {
        Flowable<Path> pathFlowable = pathCrawler.crawlDirectory(directory);
        return fileProcessor.processFiles(pathFlowable, numIntervals, maxLines);
    }

    private Report onError(Throwable throwable) {
        System.err.println("Error during source analysis: " + throwable.getMessage());
        return new Report(new HashMap<>());
    }
}