package cringe.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cringe")
public class CringeConfig {

    private boolean rootEnabled = false;
    private String rootUsername;
    private String rootPassword;

    public boolean isRootEnabled() {
        return rootEnabled;
    }

    public void setRootEnabled(boolean rootEnabled) {
        this.rootEnabled = rootEnabled;
    }

    public String getRootUsername() {
        return rootUsername;
    }

    public void setRootUsername(String rootUsername) {
        this.rootUsername = rootUsername;
    }

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword;
    }
}
