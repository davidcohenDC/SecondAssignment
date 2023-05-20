package approccio_01_task;

import approccio_01_task.boundedbuffer.Distribution;
import approccio_01_task.source_analyser.*;
import approccio_01_task.chrono.Chrono;
import approccio_01_task.utils.PerformanceUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestPerformance {

    public static final int NUM_ITERATION = 5;
    public static final String DIRECTORY = "C:\\Users\\HP\\Desktop\\UNIBO\\LaureaMagistrale";
    public static final int MAX_FILES = 10;
    public static final int NUM_INTERVALS = 10;
    public static final int MAX_LINES = 1000;

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

        //int maxThread = PerformanceUtils.getDefaultNumThread();
        Map<Integer, List<Double>> performance = new HashMap<>();
        List<Double> stepsRatioWaitComputeTime = new ArrayList<>(List.of(0.0, 0.2, 0.4, 0.6, 0.8, 1.0));
        for (Double step : stepsRatioWaitComputeTime) {
            List<Double> times = new ArrayList<>();
            for (int i = 0; i < NUM_ITERATION; i++) {
                int nThread = PerformanceUtils.getNumberThread(PerformanceUtils.getNumberCpu(), 1, step);
                Chrono crono = new Chrono();
                crono.start();
                try {
                    SourceAnalyser sourceAnalyser = new SourceAnalyserImpl(params);
                    Future<Report> report = sourceAnalyser.getReport();

                    System.out.println("\nThe distribution of files is:\n" + report.get().getDistribution());
                    System.out.println("\nThe files with the highest number of lines are: \n" + report.get().getMaxFiles());
                    crono.stop();
                    times.add((double) crono.getTime());
                    performance.put(nThread, times);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Map.Entry<Integer, List<Double>> p : performance.entrySet()) {
            double avg = p.getValue().stream()
                            .mapToDouble(d -> d)
                            .average()
                            .orElse(0.0);
            System.out.println("nThread " + p.getKey() + " " + p.getValue().toString() + " avg: " + avg);
        }
    }
}
