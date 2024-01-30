package erkamber.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfiguration {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public BCryptPasswordEncoder passwordEncoder() {

        return passwordEncoder;
    }
}