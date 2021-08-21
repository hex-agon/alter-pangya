package work.fking.pangya.common.model.server;

public enum ServerFlag {
    HIDDEN(1 << 4),
    SORT_PRIORITY(1 << 7),
    GRAND_PRIX(1 << 11);

    private final int value;

    ServerFlag(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
