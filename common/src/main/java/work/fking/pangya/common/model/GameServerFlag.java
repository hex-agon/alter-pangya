package work.fking.pangya.common.model;

public enum GameServerFlag {
    HIDDEN(1 << 4),
    SORT_PRIORITY(1 << 7),
    GRAND_PRIX(1 << 11);

    private final int value;

    GameServerFlag(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
