package work.fking.common.model;

public enum GameServerBoost {
    DOUBLE_PANG(1 << 1),
    BOOST_DOUBLE_EXP(1 << 2),
    BOOST_ANGEL_EVENT(1 << 3),
    BOOST_TRIPLE_EXP(1 << 4),
    BOOST_CLUB_MASTERY(1 << 7);

    private final int value;

    GameServerBoost(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
