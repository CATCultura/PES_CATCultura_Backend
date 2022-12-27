package cat.cultura.backend.remoterequests;

import cat.cultura.backend.utils.Score;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SimilarityServiceImpl implements SimilarityServiceAdapter {

    @Value("${similarityservice.url}")
    private String serviceURL;

    private RequestStrategy requestStrategy = new SimilarityEndPointRequest();

    @Override
    public List<Score> getMostSimilar(String query) throws IOException {
        String responseBody = requestStrategy.performRequest(serviceURL, query);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> parsedResponse = mapper.readValue(responseBody, Map.class);
        List<Score> eventList = new ArrayList<>();
        for(Map.Entry<String,String> item : parsedResponse.entrySet()) {
            eventList.add(Score.parseScore(item));
        }
        return eventList;
    }

    public void setRequestStrategy(RequestStrategy requestStrategy) {
        this.requestStrategy = requestStrategy;
    }
}
