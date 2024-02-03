package erkamber.views.venues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import erkamber.requests.ReviewRequestDto;
import erkamber.services.interfaces.ReviewService;

import java.util.function.Consumer;

@SpringComponent
@UIScope
public class ReviewAddWindows extends Dialog {

    private final TextArea descriptionField = new TextArea("Comment");
    private final TextField ratingField = new TextField("Rating");

    private final ReviewService reviewService;

    public ReviewAddWindows(int venueID, int authorID, Consumer<Integer> commentAddedCallback, ReviewService reviewService) {
        this.reviewService = reviewService;

        setWidth("400px");
        setHeight("300px");

        VerticalLayout layout = new VerticalLayout();

        descriptionField.setWidth("100%");
        ratingField.setWidth("100%");

        layout.add(descriptionField, ratingField);

        Button addButton = new Button("Add Comment", e -> {

            ReviewRequestDto reviewRequestDto = new ReviewRequestDto(descriptionField.getValue(), Integer.parseInt(ratingField.getValue()), authorID, venueID);
            reviewService.saveReview(reviewRequestDto);
            System.out.println("VenueID: " + venueID);
            System.out.println("Description: " + descriptionField.getValue());
            System.out.println("Rating: " + ratingField.getValue());

            close();

            commentAddedCallback.accept(venueID);
        });

        layout.add(addButton);

        add(layout);
    }

    public static void open(int venueID, int authorID, Consumer<Integer> commentAddedCallback, ReviewService reviewService) {
        ReviewAddWindows commentAddWindow = new ReviewAddWindows(venueID, authorID, commentAddedCallback, reviewService);
        commentAddWindow.open();
    }
}
