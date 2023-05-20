package sourceanalysis;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import org.reactivestreams.Publisher;

public class ReportTransformer  implements FlowableTransformer<Report, Pair<String, String>> {

    private final int maxFiles;

    private final int maxLines;

    private final int numInterval;

    public ReportTransformer(int maxFiles, int numInterval, int maxLines) {
        this.maxFiles = maxFiles;
        this.maxLines = maxLines;
        this.numInterval = numInterval;
    }
    @Override
    public @NonNull Publisher<Pair<String, String>> apply(@NonNull Flowable<Report> upstream) {
        return upstream.map(report -> {
            String distributionString = ReportPrinter.getDistributionString(report, numInterval, maxLines);
            String maxFilesString = ReportPrinter.getMaxFilesString(report, maxFiles);
            return Pair.of(distributionString, maxFilesString);
        });
    }
}