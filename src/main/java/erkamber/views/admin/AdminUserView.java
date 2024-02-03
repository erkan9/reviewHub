package erkamber.views.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import erkamber.dtos.ReviewDto;
import erkamber.dtos.RoleDto;
import erkamber.dtos.UserDto;
import erkamber.requests.UserRequestDto;
import erkamber.services.interfaces.ReviewService;
import erkamber.services.interfaces.RoleService;
import erkamber.services.interfaces.UserService;
import erkamber.views.MainLayout;
import erkamber.views.login.LoginView;

import java.util.ArrayList;
import java.util.List;

@Route(value = "admin-users", layout = MainLayout.class)
public class AdminUserView extends VerticalLayout {

    private final RoleService roleService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final Grid<RoleDto> roleGrid;
    private final Grid<UserDto> userGrid;
    private final TextField emailFilter;
    private final TextField userNameFilter;
    private final Button searchButton;
    private final Button clearButton;
    private final Button createRoleButton;
    private final Button userRequestButton;
    private final Button logout;

    public AdminUserView(RoleService roleService, UserService userService, ReviewService reviewService) {
        this.roleService = roleService;
        this.userService = userService;
        this.reviewService = reviewService;
        UserDto userDto = (UserDto) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(String.valueOf(UserDto.class));
        this.logout = new Button("Log out");
        logout.addClickListener(e -> {
            VaadinService.reinitializeSession(VaadinRequest.getCurrent());
            UI.getCurrent().navigate(LoginView.class);
        });
        this.roleGrid = createRoleGrid();
        this.userGrid = createUserGrid();
        this.emailFilter = new TextField("Filter by Email");
        this.userNameFilter = new TextField("Filter by Username");
        this.searchButton = new Button("Search", e -> searchUsers());
        this.clearButton = new Button("Clear Filters", e -> clearFilters());
        this.createRoleButton = new Button("Create Role", e -> openRoleCreationDialog());
        this.userRequestButton = new Button("Add User", e -> openUserRequestDialog());


        if (null != userDto && "Admin".equals(userDto.getRole().getRole())) {

            HorizontalLayout buttonLayout = new HorizontalLayout(createRoleButton, userRequestButton);
            buttonLayout.setSizeFull();
            buttonLayout.setJustifyContentMode(JustifyContentMode.END);

            HorizontalLayout headerLayout = new HorizontalLayout(new Text("ReviewHub-Admin"));
            headerLayout.setWidthFull();
            headerLayout.setAlignItems(Alignment.START);
            headerLayout.setJustifyContentMode(JustifyContentMode.END);

            headerLayout.getStyle().set("margin-right", "20px");

            add(logout, headerLayout, buttonLayout, emailFilter, userNameFilter, searchButton, clearButton, roleGrid, userGrid);

            roleGrid.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    loadAndDisplayUsers(event.getValue().getRoleID());
                }
            });

            loadAndDisplayRoles();
            loadAllUsers();

        } else {
            Notification notification = new Notification("Please log in as ADMIN to access this page");
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(5000);
            notification.getElement().getThemeList().add("error");
            notification.open();
        }
    }

    private Button createUpdateButton(UserDto userDto) {
        Button updateButton = new Button("Update");
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Dialog updateDialog = new Dialog();
        TextField firstNameField = new TextField("First Name", userDto.getUserFirstName());
        TextField lastNameField = new TextField("Last Name", userDto.getUserLastName());


        ComboBox<RoleDto> roleComboBox = new ComboBox<>("Role");
        List<RoleDto> allRoles = roleService.getAll();
        roleComboBox.setItems(allRoles);
        roleComboBox.setItemLabelGenerator(RoleDto::getRole);
        roleComboBox.setValue(userDto.getRole());

        Button confirmButton = new Button("Update", e -> {
            try {

                userDto.setUserFirstName(firstNameField.getValue());
                userDto.setUserLastName(lastNameField.getValue());
                userDto.setRole(roleComboBox.getValue());

                userService.updateUser(userDto);
                updateDialog.close();

                Notification notification = new Notification("Updated Successfully");
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setDuration(1000);
                notification.getElement().getThemeList().add("Info");
                notification.open();
                loadAndDisplayUsers(roleGrid.asSingleSelect().getValue().getRoleID());
            } catch (Exception ignored) {
            }
        });

        updateDialog.add(firstNameField, lastNameField, roleComboBox, confirmButton);

        updateButton.addClickListener(event -> updateDialog.open());
        return updateButton;
    }


    private Grid<RoleDto> createRoleGrid() {

        Grid<RoleDto> grid = new Grid<>(RoleDto.class);
        grid.setColumns("roleID", "role");
        return grid;
    }

    private Grid<UserDto> createUserGrid() {

        Grid<UserDto> grid = new Grid<>(UserDto.class);
        grid.setColumns("userID", "userName", "userEmail", "userFirstName", "userLastName", "role.role");

        grid.addComponentColumn(userDto -> createDeleteButton(userDto))
                .setHeader("Actions")
                .setKey("actions")
                .setWidth("120px")
                .setFlexGrow(0);

        grid.addComponentColumn(userDto -> createUpdateButton(userDto))
                .setHeader("Update")
                .setKey("update")
                .setWidth("120px")
                .setFlexGrow(0);

        return grid;
    }

    private Button createDeleteButton(UserDto userDto) {

        Button deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> {
            try {

                List<ReviewDto> reviewsOfUser = reviewService.findReviewsByAuthorID(userDto.getUserID());

                for (ReviewDto reviewDto : reviewsOfUser) {

                    reviewService.deleteReview(reviewDto.getReviewID());
                }
                userService.deleteByID(userDto.getUserID());
                loadAndDisplayUsers(roleGrid.asSingleSelect().getValue().getRoleID());
            } catch (Exception ex) {
                showErrorMessageDialog(ex.getMessage());
            }
        });
        return deleteButton;
    }

    private void searchUsers() {

        String emailFilterValue = emailFilter.getValue();
        String userNameFilterValue = userNameFilter.getValue();

        List<UserDto> filteredUsers = new ArrayList<>();
        if (!emailFilterValue.isEmpty() || !emailFilterValue.isBlank()) {
            UserDto searchedUser = userService.findByEmail(emailFilterValue);
            filteredUsers.add(searchedUser);
        } else if (!userNameFilterValue.isEmpty() || !userNameFilterValue.isBlank()) {
            UserDto searchedUser = userService.findByUserName(userNameFilterValue);
            filteredUsers.add(searchedUser);
        } else {
            filteredUsers = userService.getAllUsers();
        }
        userGrid.setItems(filteredUsers);
    }

    private void loadAndDisplayRoles() {

        List<RoleDto> roles = roleService.getAll();
        roleGrid.setItems(roles);
    }

    private void loadAndDisplayUsers(int roleId) {

        List<UserDto> users = userService.findUsersByRoleID(roleId);
        userGrid.setItems(users);
    }

    private void loadAllUsers() {

        List<UserDto> users = userService.getAllUsers();
        userGrid.setItems(users);
    }

    private void clearFilters() {

        emailFilter.clear();
        userNameFilter.clear();
        loadAllUsers();
        roleGrid.deselectAll();
    }

    private void openRoleCreationDialog() {

        Dialog roleCreationDialog = new Dialog();
        TextField roleNameField = new TextField("Role Name");

        Button createRoleButton = new Button("Create Role", e -> {
            String roleName = roleNameField.getValue();
            roleService.addRole(new RoleDto(roleName));
            loadAndDisplayRoles();
            roleCreationDialog.close();
        });

        roleCreationDialog.add(roleNameField, createRoleButton);
        roleCreationDialog.open();
    }

    private void openUserRequestDialog() {

        Dialog userRequestDialog = new Dialog();
        userRequestDialog.setWidth("400px");
        userRequestDialog.setHeight("500px");

        TextField userNameField = new TextField("Username");
        TextField userEmailField = new TextField("User Email");
        TextField userFirstNameField = new TextField("User First Name");
        TextField userLastNameField = new TextField("User Last Name");
        TextField userPasswordField = new TextField("User Password");
        IntegerField roleIdField = new IntegerField("Role ID");

        Button sendRequestButton = new Button("Send Request", e -> {
            try {
                UserRequestDto userRequestDto = new UserRequestDto(
                        userNameField.getValue(),
                        userEmailField.getValue(),
                        userFirstNameField.getValue(),
                        userLastNameField.getValue(),
                        userPasswordField.getValue(),
                        roleIdField.getValue()
                );
                userService.addUser(userRequestDto);
                userRequestDialog.close();
            } catch (Exception ex) {
                showErrorMessageDialog(ex.getMessage());
            }
        });

        userRequestDialog.add(userNameField, userEmailField, userFirstNameField, userLastNameField, userPasswordField, roleIdField, sendRequestButton);
        userRequestDialog.open();

        loadAllUsers();
    }

    private void showErrorMessageDialog(String message) {

        Dialog errorDialog = new Dialog();
        errorDialog.add(new Text(message));
        Button closeButton = new Button("Close", event -> errorDialog.close());
        errorDialog.add(closeButton);
        errorDialog.open();
    }
}
