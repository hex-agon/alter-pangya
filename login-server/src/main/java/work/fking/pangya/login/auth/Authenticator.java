package work.fking.pangya.login.auth;

import work.fking.pangya.common.Rand;

public interface Authenticator {

    Authenticator NOOP_AUTHENTICATOR = (username, password) -> new UserInfo(Rand.max(10000), username, username);

    UserInfo authenticate(String username, char[] password) throws Exception;
}
