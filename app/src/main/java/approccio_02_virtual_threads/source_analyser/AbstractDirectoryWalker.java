package approccio_02_virtual_threads.source_analyser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An abstract class that provides a skeleton implementation of the directory walking functionality
 * for a given directory. Concrete subclasses can extend this class and implement the `walkRec`,
 * `beforeWalk`, and `afterWalk` methods to perform the actual directory walking and file processing,
 * and to perform any necessary setup and cleanup tasks before and after the directory walking.
 * The `AbstractDirectoryWalker` class takes in a `Path` object that represents the directory to be walked,
 * a `Distribution` object that represents the distribution of files based on some criterion, and some other
 * parameters for configuring the directory walking behavior.
 */
public abstract class AbstractDirectoryWalker implements Walker {

    /**
     * The parameters for configuring the directory walking behavior.
     */
    protected final DirectoryWalkerParams params;
    protected final AtomicBoolean isRunning = new AtomicBoolean(false);

    public AbstractDirectoryWalker(DirectoryWalkerParams params) {
        this.params = params;
    }

    /**
     * Starts the directory walking process. This method creates a new thread to perform the
     * directory walking, waits for the thread to complete, and then calls the `beforeWalk` and
     * `afterWalk` methods to perform any necessary setup and cleanup tasks.
     *
     * @return true if the directory walking is successful, false otherwise
     */
    @Override
    public boolean walk() {
        this.isRunning.set(true);
        try {
            this.walkRec(this.params.getDirectory());
            this.stop();
            return true;
        } catch (Exception e) {
            System.err.println("An error occurred during the walk: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void stop() {
        this.isRunning.set(false);
        this.stopBehaviour();
    }

    /**
     * Performs the actual directory walking and file processing for the given directory.
     * This method is abstract and must be implemented by the concrete subclasses to provide
     * the specific behavior for the directory walking.
     *
     * @param directory the directory to be walked
     * @throws IOException if an I/O error occurs
     */
    protected abstract void walkRec(Path directory) throws IOException;

    protected abstract void stopBehaviour();
}
