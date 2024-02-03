package erkamber.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import erkamber.dtos.UserDto;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.services.interfaces.UserService;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {

    private final UserService userService;

    public LoginView(UserService userService) {
        this.userService = userService;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("ReviewHub");
        i18n.getHeader().setDescription("Login using user/user or admin/admin");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);

        addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            handleAuthentication(username, password);

        });

        setOpened(true);
    }

    public void handleAuthentication(String username, String password) {
        try {

            UserDto userDto = userService.login(username, password);

            VaadinService.getCurrentRequest().getWrappedSession().setAttribute(String.valueOf(UserDto.class), userDto);

            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("authenticated", true);

            showLoginSuccessMessage();

            navigateToVenuesView();

        } catch (ResourceNotFoundException e) {
            showLoginFailureMessage();
        }
    }

    private void navigateToVenuesView() {

        UI.getCurrent().getPage().executeJs("window.location.href='venues'");
    }

    private void showLoginSuccessMessage() {
        Notification.show("Login successful!");
    }

    private void showLoginFailureMessage() {
        Notification notification = new Notification("Incorrect username or password. Please try again");
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(1000);
        notification.getElement().getThemeList().add("error");
        notification.open();
    }
}
