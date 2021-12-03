package work.fking.pangya.login.networking;

public record Client(
        ClientConnection connection,
        String loginKey
) {

}
