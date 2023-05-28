import java.io.File;

public class TestOnConsole {

    public static void main(String[] args) {

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

        //new AnalyserCLI(directory, maxFiles, numIntervals, maxLines);
        SourceAnalyser sourceAnalyser = new SourceAnalyserImpl(directory, maxFiles, numIntervals, maxLines);
        sourceAnalyser.getReport();
    }
}
