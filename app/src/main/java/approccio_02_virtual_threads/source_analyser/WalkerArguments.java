package approccio_02_virtual_threads.source_analyser;

public enum WalkerArguments {
    N_FILES(0),
    DIRECTORY(1),
    NUMBER_OF_INTERVALS(2),
    MAX_LINES(3),
    ARGUMENTS_SIZE(4);

    private final int value;

    WalkerArguments(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}