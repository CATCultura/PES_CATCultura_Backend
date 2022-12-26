package cat.cultura.backend.remoterequests;

import cat.cultura.backend.utils.Score;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SimilarityServiceImpl implements SimilarityServiceAdapter {

    @Value("${similarityservice.url}")
    private String serviceURL;

    private String performRequest(String query) throws IOException {
        URL url = new URL(serviceURL+"?q="+ URLEncoder.encode(query, StandardCharsets.UTF_8));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        }
        else {
            return null;
        }
    }
    @Override
    public List<Score> getMostSimilar(String query) throws IOException {
        String responseBody = performRequest(query);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Double> parsedResponse = mapper.readValue(responseBody, Map.class);
        List<Score> eventList = new ArrayList<>();
        for(Map.Entry<String,Double> item : parsedResponse.entrySet()) {
            eventList.add(Score.parseScore(item));
        }
        return eventList;
    }
}
