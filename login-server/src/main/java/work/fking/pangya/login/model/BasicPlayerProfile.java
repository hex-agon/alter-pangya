package work.fking.pangya.login.model;

public record BasicPlayerProfile() {

    public static BasicPlayerProfile blank() {
        return new BasicPlayerProfile();
    }
}
