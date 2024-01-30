package erkamber.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private final String notFoundResourceName;

    public ResourceNotFoundException(String errorMessage, String notFoundResourceName) {
        super(errorMessage);
        this.notFoundResourceName = notFoundResourceName;
    }

    public String getNotFoundResourceName() {
        return notFoundResourceName;
    }
}
