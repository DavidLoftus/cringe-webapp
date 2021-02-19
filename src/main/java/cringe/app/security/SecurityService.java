package cringe.app.security;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
