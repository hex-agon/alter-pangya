package work.fking.pangya.game.model;

public enum HoleMode {
    FRONT,
    BACK,
    RANDOM,
    SHUFFLE,
    REPEAT,
    SUFFLE_COURSE;

    private static final HoleMode[] VALUES = values();

    public static HoleMode forId(int id) {
        if (id < VALUES.length) {
            return VALUES[id];
        }
        return null;
    }
}
