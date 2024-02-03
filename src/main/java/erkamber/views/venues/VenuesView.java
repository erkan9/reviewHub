package erkamber.views.venues;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import erkamber.dtos.CategoryDto;
import erkamber.dtos.ReviewDto;
import erkamber.dtos.UserDto;
import erkamber.dtos.VenueDto;
import erkamber.services.interfaces.CategoryService;
import erkamber.services.interfaces.ReviewService;
import erkamber.services.interfaces.UserService;
import erkamber.services.interfaces.VenueService;
import erkamber.views.MainLayout;
import erkamber.views.login.LoginView;

import java.util.List;

@PageTitle("All Venues")
@Route(value = "venues", layout = MainLayout.class)
public class VenuesView extends VerticalLayout {

    private final VenueService venueService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final Grid<VenueDto> venuesGrid;
    private final Grid<ReviewDto> commentsGrid;
    private final TextField cityFilter;
    private final Button filterButton;
    private final Button clearButton;
    private final Button logout;
    private final CategoryService categoryService;
    private final ComboBox<String> categoryComboBox;
    private UserDto userDto;
    int venueID;

    public VenuesView(VenueService venueService, ReviewService reviewService, UserService userService, CategoryService categoryService) {
        this.venueService = venueService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.logout = new Button("Log out");
        logout.addClickListener(e -> {
            VaadinService.reinitializeSession(VaadinRequest.getCurrent());
            UI.getCurrent().navigate(LoginView.class); // Redirect to the login view or any other view after logout
        });

        this.categoryService = categoryService;
        this.venuesGrid = createVenuesGrid();
        this.commentsGrid = createCommentsGrid();
        this.cityFilter = new TextField("Filter by City");
        this.categoryComboBox = new ComboBox<>("Filter by Category");

        List<CategoryDto> allCategoriesDto = categoryService.getAllCategories();
        List<String> categoryNames = allCategoriesDto.stream()
                .map(CategoryDto::getCategory)
                .toList();
        categoryComboBox.setItems(categoryNames);
        this.filterButton = new Button("Search", e -> applyFilter());
        this.clearButton = new Button("Clear", e -> clear());

        userDto = (UserDto) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(String.valueOf(UserDto.class));

        if (null != userDto) {
            initLayout();
            loadAndDisplayVenues();
        } else {
            Notification notification = new Notification("Please log in order to access this page");
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(5000);
            notification.getElement().getThemeList().add("error");
            notification.open();
        }
    }

    private void initLayout() {

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        add(logout, cityFilter, categoryComboBox, filterButton, clearButton, venuesGrid);

        add(commentsGrid);
    }

    private Grid<VenueDto> createVenuesGrid() {

        Grid<VenueDto> grid = new Grid<>(VenueDto.class);
        grid.setColumns("venueName", "venueDescription", "venueCity", "venueAddress", "phoneNumber", "email",
                "averageRating", "reviewCount", "category");

        grid.getColumnByKey("averageRating").setHeader("Average Rating");
        grid.getColumnByKey("reviewCount").setHeader("Review Count");

        userDto = (UserDto) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(String.valueOf(UserDto.class));

        if (null != userDto && !"Admin".equals(userDto.getRole().getRole())) {
            grid.addComponentColumn(venue -> {
                Button addCommentButton = new Button("Add Comment", e -> openAddCommentWindow(venue.getVenueID()));
                return addCommentButton;
            }).setHeader("Actions");
        }

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                loadAndDisplayComments(event.getValue().getVenueID());
            }
        });

        return grid;
    }

    private void openAddCommentWindow(int venueID) {

            ReviewAddWindows.open(venueID, userDto.getUserID(), this::loadAndDisplayComments, reviewService);
    }

    private Grid<ReviewDto> createCommentsGrid() {

        Grid<ReviewDto> grid = new Grid<>(ReviewDto.class);
        grid.setColumns("description", "rating", "authorName", "venueName");

        grid.addComponentColumn(review -> {
            if (review.getAuthorName().equals(userDto.getUserName())) {
                Button deleteButton = new Button("Delete", e -> deleteReview(review.getReviewID()));
                return deleteButton;
            } else {
                return new VerticalLayout();
            }
        }).setHeader("Actions");

        grid.asSingleSelect().addValueChangeListener(event -> {
        });

        return grid;
    }

    private void deleteReview(int reviewID) {

        reviewService.deleteReview(reviewID);

        loadAndDisplayComments(venueID);
        loadAndDisplayVenues();
    }

    private void loadAndDisplayVenues() {

        List<VenueDto> allVenues = venueService.findAllVenues();

        venuesGrid.setItems(allVenues);
    }

    private void applyFilter() {

        String cityFilterValue = cityFilter.getValue();
        String categoryFilterValue = categoryComboBox.getValue();

        List<VenueDto> filteredVenues = venueService.findVenueByVenueCityAndCategoryCategory(cityFilterValue, categoryFilterValue);
        venuesGrid.setItems(filteredVenues);
    }

    private void clear() {

        cityFilter.clear();
        loadAndDisplayVenues();
        commentsGrid.setItems();
    }

    private void loadAndDisplayComments(int venueId) {

        List<ReviewDto> venueComments = reviewService.findReviewsByVenueID(venueId);
        commentsGrid.setItems(venueComments);
    }
}
