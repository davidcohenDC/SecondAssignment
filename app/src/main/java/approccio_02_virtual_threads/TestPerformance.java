package approccio_02_virtual_threads;

import approccio_02_virtual_threads.boundedbuffer.Distribution;
import approccio_02_virtual_threads.chrono.Chrono;
import approccio_02_virtual_threads.source_analyser.*;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestPerformance {

    private static final int NUM_ITERATION = 5;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != WalkerArguments.ARGUMENTS_SIZE.getValue()) {
            System.out.println("Usage: <max number of files> <directory> <number of intervals> <max number of lines>");
            System.exit(1);
        }
        String directory = args[WalkerArguments.DIRECTORY.getValue()];
        int maxFiles = Integer.parseInt(args[WalkerArguments.N_FILES.getValue()]);
        int numIntervals = Integer.parseInt(args[WalkerArguments.NUMBER_OF_INTERVALS.getValue()]);
        int maxLines = Integer.parseInt(args[WalkerArguments.MAX_LINES.getValue()]);

        if (numIntervals <= 0 || maxLines <= 0) {
            System.out.println("The number of intervals and the max length of interval must be greater than 0");
            System.exit(1);
        }

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("The directory " + directory + " does not exist");
            System.exit(1);
        }

        Distribution<Integer, Path> distribution = new Distribution<>();
        DirectoryWalkerParams params = DirectoryWalkerParams.builder()
                .directory(dir.toPath())
                .maxFiles(maxFiles)
                .numIntervals(numIntervals)
                .maxLines(maxLines)
                .distribution(distribution)
                .build();

        List<Double> performance = new ArrayList<>();
        for (int i = 0; i < NUM_ITERATION; i++) {
            long time = runBenchmark(params);
            performance.add((double) time);
        }
        performance.forEach(e -> System.out.println("Time: " + e + " (ms)"));
        double avg = performance.stream().mapToDouble(d -> d).average().orElse(0.0);
        double min = performance.stream().min(Comparator.naturalOrder()).orElse(0.0);
        System.out.println("\nmin: " + min);
        System.out.println("avg: " + avg);
        System.exit(0);
    }

    private static long runBenchmark(DirectoryWalkerParams params) throws InterruptedException, ExecutionException {
        Chrono crono = new Chrono();
        crono.start();
        SourceAnalyser sourceAnalyser = new SourceAnalyserImpl(params);
        Future<Report> futureReport = sourceAnalyser.getReport();
        Report report = futureReport.get(); // report result ignored
        crono.stop();
        return crono.getTime();
    }
}
