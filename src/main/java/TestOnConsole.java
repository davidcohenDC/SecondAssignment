import io.reactivex.rxjava3.core.Flowable;
import sourceanalysis.*;
import java.io.File;

public class TestOnConsole {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != Constants.Arguments.ARGUMENTS_SIZE) {
            System.out.println("Usage: <directory> <number of intervals> <max length of interval>");
            System.exit(1);
        }

        String directory = args[Constants.Arguments.DIRECTORY];
        int maxFiles = Integer.parseInt(args[Constants.Arguments.N_FILES]);
        int numIntervals = Integer.parseInt(args[Constants.Arguments.NUMBER_OF_INTERVALS]);
        int maxLength = Integer.parseInt(args[Constants.Arguments.MAX_LINES]);

        if (numIntervals <= 0 || maxLength <= 0) {
            System.out.println("The number of intervals and the max length of interval must be greater than 0");
            System.exit(1);
        }
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("The directory " + directory + " does not exist");
            System.exit(1);
        }

        int numCores = Runtime.getRuntime().availableProcessors()+1;
        SourceAnalyser sourceAnalyser = new SourceAnalyserImpl(
                new PathCrawler(), new FileProcessor(numCores), numIntervals, maxLength, dir.toPath());

        sourceAnalyser.getReport()
                .flatMapPublisher(report ->new ReportTransformer(maxFiles).apply(Flowable.just(report)))
                .toList()
                .blockingSubscribe(
                        pair -> {
                            System.out.println("Received final distribution: " + pair.get(0).getLeft());
                            System.out.println("Received final max files: " + pair.get(0).getRight());
                        },
                        error -> System.err.println("Error occurred: " + error)
                );
    }
}
