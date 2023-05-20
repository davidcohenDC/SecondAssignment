package approccio_01_task;

import approccio_01_task.boundedbuffer.Distribution;
import approccio_01_task.source_analyser.DirectoryWalkerParams;
import approccio_01_task.source_analyser.Report;
import approccio_01_task.source_analyser.SourceAnalyser;
import approccio_01_task.source_analyser.SourceAnalyserImpl;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestOnConsole {

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

        SourceAnalyser sourceAnalyser = new SourceAnalyserImpl(params);
        Future<Report> report = sourceAnalyser.getReport();

        System.out.println("\nThe distribution of files is:\n" + report.get().getDistribution());
        System.out.println("\nThe files with the highest number of lines are: \n" + report.get().getMaxFiles());
        //System.out.println("\nTime to execute the operations: " + crono.getTime() + " (milliseconds)");
        System.exit(0);
    }
}