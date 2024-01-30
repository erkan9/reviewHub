package erkamber.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CharAndNumberPatternConfiguration {

    String charAndNumberPattern = "([A-Za-z_]{5,15})\\w+";

    public String getCharAndNumberPattern() {

        return charAndNumberPattern;
    }
}
