package erkamber.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import erkamber.dtos.UserDto;
import erkamber.views.admin.AdminUserView;
import erkamber.views.admin.AdminVenueView;
import erkamber.views.venues.VenuesView;

public class MainLayout extends AppLayout {

    UserDto userDto;

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("ReviewHub");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {

        userDto = (UserDto) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(String.valueOf(UserDto.class));

        if (null != userDto) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Venues", VenuesView.class)
            ));
        }
        if (null != userDto && "Admin".equals(userDto.getRole().getRole())) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Admin Users", AdminUserView.class)
            ));
            addToDrawer(new VerticalLayout(
                    new RouterLink("Admin Venues", AdminVenueView.class)
            ));
        }
    }
}