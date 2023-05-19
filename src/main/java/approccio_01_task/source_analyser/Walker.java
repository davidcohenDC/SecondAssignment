package approccio_01_task.source_analyser;

import java.io.IOException;

public interface Walker {

    /**
     * Walk the directory and print the longest line in each file
     *
     * @throws IOException if an I/O error occurs
     */
    boolean walk() throws IOException, InterruptedException;

    void stop();

}


