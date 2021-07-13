package work.fking.pangya.common.model.server;

public enum GameServerBoost {
    DOUBLE_PANG(1 << 1),
    DOUBLE_EXP(1 << 2),
    ANGEL_EVENT(1 << 3),
    TRIPLE_EXP(1 << 4),
    CLUB_MASTERY(1 << 7);

    private final int value;

    GameServerBoost(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
