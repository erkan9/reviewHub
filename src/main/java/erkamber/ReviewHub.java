package erkamber;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfigurationPackage
@Import({org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.class})
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ReviewHub {
    public static void main(String[] args) {
        SpringApplication.run(ReviewHub.class, args);
        {
        }
    }
}