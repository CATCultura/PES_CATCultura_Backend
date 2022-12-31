package cat.cultura.backend.remoterequests;

import java.io.IOException;

public interface RequestStrategy {

    String performRequest(String requestUrl, String query) throws IOException;
}
