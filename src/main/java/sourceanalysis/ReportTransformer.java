package sourceanalysis;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import org.reactivestreams.Publisher;

public class ReportTransformer  implements FlowableTransformer<Report, Pair<String, String>> {

    private final int maxFiles;

    public ReportTransformer(int maxFiles) {
        this.maxFiles = maxFiles;
    }
    @Override
    public @NonNull Publisher<Pair<String, String>> apply(@NonNull Flowable<Report> upstream) {
        return upstream.map(report -> {
            String distributionString = ReportPrinter.getDistributionString(report);
            String maxFilesString = ReportPrinter.getMaxFilesString(report, maxFiles);
            return Pair.of(distributionString, maxFilesString);
        });
    }
}