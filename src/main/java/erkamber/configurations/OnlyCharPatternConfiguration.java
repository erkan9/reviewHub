package erkamber.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyCharPatternConfiguration {

    String onlyCharPattern = "^[A-Za-z]+$";

    public String getOnlyCharPattern() {

        return onlyCharPattern;
    }
}