package work.fking.pangya.login.domain.port;

import work.fking.pangya.login.networking.Client;

public interface LoginPort {

    void login(Client client, String username, char[] password);

    void reconnect(String username, int userId, String loginKey);
}
