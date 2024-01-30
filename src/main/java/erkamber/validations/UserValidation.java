package erkamber.validations;

import erkamber.configurations.CharAndNumberPatternConfiguration;
import erkamber.configurations.OnlyCharPatternConfiguration;
import erkamber.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private final OnlyCharPatternConfiguration onlyCharPatternConfiguration;

    private final CharAndNumberPatternConfiguration charAndNumberPatternConfiguration;

    public UserValidation(OnlyCharPatternConfiguration onlyCharPatternConfiguration, CharAndNumberPatternConfiguration charAndNumberPatternConfiguration) {
        this.onlyCharPatternConfiguration = onlyCharPatternConfiguration;
        this.charAndNumberPatternConfiguration = charAndNumberPatternConfiguration;
    }

    public boolean isUserFirstOrLastNameValid(String name) {

        String userFirstLastNamePatterns = onlyCharPatternConfiguration.getOnlyCharPattern();

        return Pattern.compile(userFirstLastNamePatterns).matcher(name).matches();
    }

    public boolean isListEmpty(List<User> listOfNews) {

        return listOfNews == null || listOfNews.isEmpty();
    }

    public boolean isUserNameValid(String userName) {

        String userNamePattern = charAndNumberPatternConfiguration.getCharAndNumberPattern();

        return Pattern.compile(userNamePattern).matcher(userName).matches();
    }
}
