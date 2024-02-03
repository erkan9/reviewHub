package erkamber.views.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import erkamber.dtos.CategoryDto;
import erkamber.dtos.ReviewDto;
import erkamber.dtos.UserDto;
import erkamber.dtos.VenueDto;
import erkamber.requests.CategoryRequestDto;
import erkamber.requests.VenueRequestDto;
import erkamber.services.interfaces.CategoryService;
import erkamber.services.interfaces.ReviewService;
import erkamber.services.interfaces.VenueService;
import erkamber.views.MainLayout;
import erkamber.views.login.LoginView;

import java.util.List;

@Route(value = "admin-venues", layout = MainLayout.class)
public class AdminVenueView extends VerticalLayout {

    private final CategoryService categoryService;
    private final VenueService venueService;
    private final ReviewService reviewService;
    private final Grid<CategoryDto> categoryGrid;
    private final Grid<VenueDto> venueGrid;
    private final TextField cityFilter;
    private final Button searchButton;
    private final Button clearButton;
    private final Button logout;
    private final Button createCategoryButton;
    private final Button addVenueButton;

    public AdminVenueView(CategoryService categoryService, VenueService venueService, ReviewService reviewService) {
        this.categoryService = categoryService;
        this.venueService = venueService;
        this.reviewService = reviewService;
        this.logout = new Button("Log out");
        UserDto userDto = (UserDto) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(String.valueOf(UserDto.class));
        logout.addClickListener(e -> {
            VaadinService.reinitializeSession(VaadinRequest.getCurrent());
            UI.getCurrent().navigate(LoginView.class);
        });
        this.categoryGrid = createCategoryGrid();
        this.venueGrid = createVenueGrid();
        this.cityFilter = new TextField("Filter by City");
        this.searchButton = new Button("Search", e -> searchVenues());
        this.clearButton = new Button("Clear Filters", e -> clearFilters());
        this.createCategoryButton = new Button("Create Category", e -> openCategoryCreationDialog());
        this.addVenueButton = new Button("Add Venue", e -> openVenueAdditionDialog());

        if (null != userDto && "Admin".equals(userDto.getRole().getRole())) {

            HorizontalLayout buttonLayout = new HorizontalLayout(createCategoryButton, addVenueButton);
            buttonLayout.setSizeFull();
            buttonLayout.setJustifyContentMode(JustifyContentMode.END);

            HorizontalLayout headerLayout = new HorizontalLayout(new Text("ReviewHub-Admin - Venues"));
            headerLayout.setWidthFull();
            headerLayout.setAlignItems(Alignment.START);
            headerLayout.setJustifyContentMode(JustifyContentMode.END);

            headerLayout.getStyle().set("margin-right", "20px");

            add(logout, headerLayout, buttonLayout, cityFilter, searchButton, clearButton, categoryGrid, venueGrid);

            categoryGrid.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    loadAndDisplayVenues(event.getValue().getCategoryID());
                }
            });

            loadAndDisplayCategories();
            loadAllVenues();

        } else {
            Notification notification = new Notification("Please log in as ADMIN to access this page");
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(5000);
            notification.getElement().getThemeList().add("error");
            notification.open();
        }
    }

    private Grid<CategoryDto> createCategoryGrid() {

        Grid<CategoryDto> grid = new Grid<>(CategoryDto.class);
        grid.setColumns("categoryID", "category");
        return grid;
    }

    private Grid<VenueDto> createVenueGrid() {

        Grid<VenueDto> grid = new Grid<>(VenueDto.class);
        grid.setColumns("venueID", "venueName", "venueDescription", "venueCity", "venueAddress", "phoneNumber", "email",
                "averageRating", "reviewCount", "category");

        grid.addComponentColumn(this::createDeleteButton)
                .setHeader("Actions")
                .setKey("actions")
                .setWidth("120px")
                .setFlexGrow(0);

        grid.addComponentColumn(this::createUpdateButton)
                .setHeader("Update")
                .setKey("update")
                .setWidth("120px")
                .setFlexGrow(0);

        return grid;
    }

    private Button createDeleteButton(VenueDto venueDto) {

        Button deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> {
            try {
                List<ReviewDto> reviewsOfUser = reviewService.findReviewsByVenueID(venueDto.getVenueID());

                for (ReviewDto reviewDto : reviewsOfUser) {

                    reviewService.deleteReview(reviewDto.getReviewID());
                }
                venueService.deleteByVenueID(venueDto.getVenueID());
                loadAndDisplayVenues(categoryGrid.asSingleSelect().getValue().getCategoryID());
            } catch (Exception ex) {
                showErrorMessageDialog(ex.getMessage());
            }
        });
        return deleteButton;
    }

    private Button createUpdateButton(VenueDto venueDto) {
        Button updateButton = new Button("Update");
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Dialog updateDialog = new Dialog();
        TextField phoneField = new TextField("Phone", venueDto.getPhoneNumber());
        TextField addressField = new TextField("Address", venueDto.getVenueAddress());
        TextField cityField = new TextField("City", venueDto.getVenueCity());
        TextArea descriptionArea = new TextArea("Description", venueDto.getVenueDescription());

        Button confirmButton = new Button("Update", e -> {
            try {
                venueDto.setPhoneNumber(phoneField.getValue());
                venueDto.setVenueAddress(addressField.getValue());
                venueDto.setVenueCity(cityField.getValue());
                venueDto.setVenueDescription(descriptionArea.getValue());

                venueService.updateVenue(venueDto.getVenueID(), phoneField.getValue(), addressField.getValue(), cityField.getValue(), descriptionArea.getValue());

                updateDialog.close();
                loadAndDisplayVenues(categoryGrid.asSingleSelect().getValue().getCategoryID());
            } catch (Exception ignored) {
            }
        });

        // Add components to the dialog
        updateDialog.add(phoneField, addressField, cityField, descriptionArea, confirmButton);

        updateButton.addClickListener(event -> updateDialog.open());
        return updateButton;
    }

    private void searchVenues() {
        String cityFilterValue = cityFilter.getValue();

        List<VenueDto> filteredVenues;
        if (!cityFilterValue.isEmpty() || !cityFilterValue.isBlank()) {
            filteredVenues = venueService.findVenueByVenueCity(cityFilterValue);
        } else {
            filteredVenues = venueService.findAllVenues();
        }
        venueGrid.setItems(filteredVenues);
    }

    private void loadAndDisplayCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        categoryGrid.setItems(categories);
    }

    private void loadAndDisplayVenues(int categoryId) {
        List<VenueDto> venues = venueService.findVenuesByCategoryID(categoryId);
        venueGrid.setItems(venues);
    }

    private void loadAllVenues() {
        List<VenueDto> venues = venueService.findAllVenues();
        venueGrid.setItems(venues);
    }

    private void clearFilters() {
        cityFilter.clear();
        loadAllVenues();
        categoryGrid.deselectAll();
    }

    private void openCategoryCreationDialog() {
        Dialog categoryCreationDialog = new Dialog();
        TextField categoryNameField = new TextField("Category Name");
        Button createCategoryButton = new Button("Create Category", e -> {
            try {
                String categoryName = categoryNameField.getValue();
                categoryService.saveCategory(new CategoryRequestDto(categoryName));
                loadAndDisplayCategories();
                categoryCreationDialog.close();
                showSuccessMessageDialog("Category created successfully!");
            } catch (Exception ex) {
                showErrorMessageDialog(ex.getMessage());
            }
        });
        categoryCreationDialog.add(categoryNameField, createCategoryButton);
        categoryCreationDialog.open();
    }

    private void showSuccessMessageDialog(String message) {
        Dialog successDialog = new Dialog();
        successDialog.add(new Text(message));
        Button closeButton = new Button("Close", event -> successDialog.close());
        successDialog.add(closeButton);
        successDialog.open();
    }

    private void openVenueAdditionDialog() {

        Dialog venueAdditionDialog = new Dialog();
        venueAdditionDialog.setWidth("400px");
        venueAdditionDialog.setHeight("500px");

        TextField venueNameField = new TextField("Venue Name");
        TextField venueDescription = new TextField("Description");
        TextField cityField = new TextField("City");
        TextField address = new TextField("Address");
        TextField phone = new TextField("Phone");
        EmailField emailField = new EmailField("Email");
        IntegerField categoryIdField = new IntegerField("Category ID");

        Button addVenueButton = new Button("Add Venue", e -> {
            try {
                VenueRequestDto venueRequestDto = new VenueRequestDto(
                        venueNameField.getValue(),
                        venueDescription.getValue(),
                        cityField.getValue(),
                        address.getValue(),
                        phone.getValue(),
                        emailField.getValue(),
                        categoryIdField.getValue()
                );
                venueService.saveVenue(venueRequestDto);
                venueAdditionDialog.close();
                showSuccessMessageDialog("Venue created successfully!");
            } catch (Exception ex) {
                showErrorMessageDialog(ex.getMessage());
            }
        });

        venueAdditionDialog.add(venueNameField, venueDescription, cityField, address, phone, emailField, categoryIdField, addVenueButton);
        venueAdditionDialog.open();

        loadAllVenues();
    }

    private void showErrorMessageDialog(String message) {

        Dialog errorDialog = new Dialog();
        errorDialog.add(new Text(message));
        Button closeButton = new Button("Close", event -> errorDialog.close());
        errorDialog.add(closeButton);
        errorDialog.open();
    }
}
