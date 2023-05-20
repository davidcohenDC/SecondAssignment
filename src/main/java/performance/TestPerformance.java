package performance;

import io.reactivex.rxjava3.core.Flowable;
import sourceanalysis.*;

import java.io.File;
import java.nio.file.Path;

public class TestPerformance {
    public static void main(String[] args) {
        int[] coreCounts = { 1, 2, 4, 8, Runtime.getRuntime().availableProcessors()+1 }; // Array contenente il numero di core su cui eseguire i test
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

        for (int cores : coreCounts) {
            System.out.println("=== Testing with " + cores + " cores ===");
            long totalTime = runBenchmark(cores,dir.toPath(), numIntervals, maxLength, maxFiles);
            System.out.println("Total time: " + totalTime + "ms\n");
        }
    }

    private static long runBenchmark(int cores, Path directory, int numIntervals, int maxLength, int maxFiles) {
        Cron benchmarkCron = new Cron();
        benchmarkCron.start();

        // Crea una istanza di FileProcessor con il numero di core specificato
        FileProcessor fileProcessor = new FileProcessor(cores);

        // Crea un'istanza di SourceAnalyserImpl con le dipendenze necessarie
        PathCrawler pathCrawler = new PathCrawler();
        SourceAnalyzerImpl sourceAnalyser = new SourceAnalyzerImpl(pathCrawler, fileProcessor, numIntervals, maxLength, directory);

        sourceAnalyser.getReport()
                .flatMapPublisher(report ->new ReportTransformer(maxFiles, numIntervals, maxLength).apply(Flowable.just(report)))
                .toList()
                .blockingSubscribe(
                        pair -> {
                            System.out.println("Received final distribution: " + pair.get(0).getLeft());
                            System.out.println("Received final max files: " + pair.get(0).getRight());
                        },
                        error -> System.err.println("Error occurred: " + error)
                );

        benchmarkCron.stop();
        return benchmarkCron.getTime();
    }

}