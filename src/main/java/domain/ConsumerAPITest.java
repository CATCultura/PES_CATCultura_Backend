package domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsumerAPITest {

    public static void main(String[] args) throws IOException {

        URL url = new URL("http://127.0.0.1:5001/get-all");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        if (con.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(con.getInputStream());
            for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
                JsonNode child_node = it.next();
//                Event e = mapper.treeToValue(child_node,Event.class);
                Event e = mapper.treeToValue(child_node,Event.class);
                System.out.println(e.getCodi());
            }



        }

    }
}
