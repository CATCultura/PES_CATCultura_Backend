package cat.cultura.backend.exceptions;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException() {
            super("Route not found");
        }

    public RouteNotFoundException(String message) {
            super(message);
        }
}
