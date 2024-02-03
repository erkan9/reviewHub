package erkamber.views.registration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import erkamber.requests.UserRequestDto;
import erkamber.services.interfaces.UserService;

@AnonymousAllowed
@PageTitle("Register")
@Route(value = "register")
public class RegistrationView extends FormLayout {

    private final UserService userService;

    public RegistrationView(UserService userService) {
        this.userService = userService;

        TextField userNameField = new TextField("Username");
        EmailField userEmailField = new EmailField("Email");
        TextField userFirstNameField = new TextField("First Name");
        TextField userLastNameField = new TextField("Last Name");
        PasswordField userPasswordField = new PasswordField("Password");

        Button registerButton = new Button("Register", e -> handleRegistration(
                userNameField.getValue(),
                userEmailField.getValue(),
                userFirstNameField.getValue(),
                userLastNameField.getValue(),
                userPasswordField.getValue()
        ));

        add(userNameField, userEmailField, userFirstNameField, userLastNameField, userPasswordField, registerButton);
    }

    private void handleRegistration(String userName, String userEmail, String userFirstName, String userLastName, String userPassword) {

        final int DEFAULT_USER_ROLE_ID = 1;

        try {
            UserRequestDto userRequestDto = new UserRequestDto(userName, userEmail, userFirstName, userLastName, userPassword, DEFAULT_USER_ROLE_ID);
            userService.addUser(userRequestDto);
            showRegistrationSuccessMessage();
            navigateToLogIn();
        } catch (Exception ex) {
            showRegistrationFailureMessage(ex.getMessage());
        }
    }

    private void navigateToLogIn() {
        UI.getCurrent().navigate("login");
    }

    private void showRegistrationSuccessMessage() {
        Notification notification = new Notification("Successful Registration!");
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(5000);
    }

    private void showRegistrationFailureMessage(String message) {
        Notification.show(message);
    }
}
