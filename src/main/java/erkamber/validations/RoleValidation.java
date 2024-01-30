package erkamber.validations;

import erkamber.configurations.OnlyCharPatternConfiguration;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RoleValidation {

    private final OnlyCharPatternConfiguration onlyCharPatternConfiguration;

    public RoleValidation(OnlyCharPatternConfiguration onlyCharPatternConfiguration) {
        this.onlyCharPatternConfiguration = onlyCharPatternConfiguration;
    }

    public boolean isRoleValid(String role) {

        String onlyCharPattern = onlyCharPatternConfiguration.getOnlyCharPattern();

        return Pattern.compile(onlyCharPattern).matcher(role).matches();
    }
}
