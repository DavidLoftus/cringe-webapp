package cringe.app.config;

import cringe.app.util.CustomDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
    @Bean
    public CustomDialect customDialect() {
        return new CustomDialect();
    }
}
