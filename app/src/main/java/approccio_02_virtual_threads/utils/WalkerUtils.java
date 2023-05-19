package approccio_02_virtual_threads.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WalkerUtils {
    public static int countLines(Path file) throws IOException {
        try (Stream<String> fileStream = Files.lines(file)) {
            return (int) fileStream
                    .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("//"))  // ignore empty lines
                    .filter(line -> !line.matches(".*\\*\\/.*"))  // ignore inline comments
                    .count();  // count the remaining lines.count();
        } catch (UncheckedIOException e) {
            if (e.getCause() instanceof MalformedInputException) {
                System.out.println("Ignored file " + file + " because malformed");
            }
            return 0;
        }
    }
    public static boolean isRegularJavaFile(Path path) {
        return Files.isRegularFile(path) && path.getFileName().toString().endsWith(".java");
    }
    public static boolean isDirectoryNotHidden(Path path) throws IOException {
        return Files.isDirectory(path) && !Files.isHidden(path);
    }
}