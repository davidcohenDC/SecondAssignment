package sourceanalysis;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileValidationUtils {

    public static boolean isValidLine(String line) {
        return !line.trim().isEmpty()
                && !line.trim().startsWith("//")
                && !line.matches(".*\\*\\/.*");
    }

    public static boolean isJavaFile(Path file) {
        return Files.isRegularFile(file) && file.toString().endsWith(".java");
    }
}
