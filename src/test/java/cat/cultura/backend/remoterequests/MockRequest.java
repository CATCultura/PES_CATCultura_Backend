package cat.cultura.backend.remoterequests;

import java.io.IOException;

public class MockRequest implements RequestStrategy{
    @Override
    public String performRequest(String requestUrl, String query) throws IOException {
        return "{" +
                "\"1234\" : 4.9," +
                "\"5678\" : NaN" +
                "}";
    }
}
