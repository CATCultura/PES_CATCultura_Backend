package cat.cultura.backend.service;

import cat.cultura.backend.utils.Score;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class SemanticService {

    @Value("${similarityservice.url}")
    private String serviceURL;
    public List<Score> getEventListByQuery(String query) throws IOException {
        URL url = new URL(serviceURL+"?q="+URLEncoder.encode(query, StandardCharsets.UTF_8));
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
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(content.toString(), List.class);
        }
        else {
            return null;
        }
    }


}
