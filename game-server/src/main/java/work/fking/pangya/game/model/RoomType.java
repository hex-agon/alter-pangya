package work.fking.pangya.game.model;

public enum RoomType {
    VERSUS(0),
    CHAT(2),
    TOURNAMENT(4),
    PANG_BATTLE(10),
    PRACTICE(19);

    private final int id;

    RoomType(int id) {
        this.id = id;
    }

    public static RoomType forId(int id) {
        return switch (id) {
            case 0 -> VERSUS;
            case 2 -> CHAT;
            case 4 -> TOURNAMENT;
            case 10 -> PANG_BATTLE;
            case 19 -> PRACTICE;
            default -> null;
        };
    }

    public int id() {
        return id;
    }
}
