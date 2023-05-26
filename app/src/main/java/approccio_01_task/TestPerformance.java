package approccio_01_task;

import approccio_01_task.boundedbuffer.Distribution;
import approccio_01_task.source_analyser.*;
import approccio_01_task.chrono.Chrono;
import approccio_01_task.utils.PerformanceUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestPerformance {

    public static final String DIRECTORY = "C:\\Users\\HP\\Desktop\\UNIBO\\LaureaMagistrale";
    public static final int MAX_FILES = 10;
    public static final int NUM_INTERVALS = 10;
    public static final int MAX_LINES = 100;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /*if (args.length != WalkerArguments.ARGUMENTS_SIZE.getValue()) {
            System.out.println("Usage: <max number of files> <directory> <number of intervals> <max number of lines>");
            System.exit(1);
        }*/
        String directory = DIRECTORY;//args[WalkerArguments.DIRECTORY.getValue()];
        int maxFiles = MAX_FILES; //Integer.parseInt(args[WalkerArguments.N_FILES.getValue()]);
        int numIntervals = NUM_INTERVALS; //Integer.parseInt(args[WalkerArguments.NUMBER_OF_INTERVALS.getValue()]);
        int maxLines = MAX_LINES;//Integer.parseInt(args[WalkerArguments.MAX_LINES.getValue()]);

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

        Map<Integer, Long> performance = new HashMap<>();
        List<Double> stepsRatioWaitComputeTime = List.of(0.0, 0.2, 0.4, 0.6, 0.8, 1.0);
        for (Double step : stepsRatioWaitComputeTime) {
            int poolSize = PerformanceUtils.getNumberThread(PerformanceUtils.getNumberCpu(), 1, step);
            long time = runBenchmark(params, poolSize);
            performance.put(poolSize, time);
        }
        performance.forEach((k, v) -> System.out.println("poolSize " + k + ": " + v + " (ms)"));
        long min = performance.values().stream().min(Comparator.naturalOrder()).get();
        System.out.println("\nmin time: " + min);
        System.exit(0);
    }

    private static long runBenchmark(DirectoryWalkerParams params, int poolSize) throws InterruptedException, ExecutionException {
        Chrono crono = new Chrono();
        crono.start();
        SourceAnalyser sourceAnalyser = new SourceAnalyserImpl(params, poolSize);
        Future<Report> futureReport = sourceAnalyser.getReport();
        futureReport.get(); // report result ignored
        //futureReport.cancel(true);
        crono.stop();
        return crono.getTime();
    }
}
