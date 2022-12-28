package cat.cultura.backend.remoterequests;

import cat.cultura.backend.utils.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityServiceImplTest {


    @Test
    void getMostSimilar() throws IOException {
        SimilarityServiceImpl simService = new SimilarityServiceImpl();
        simService.setRequestStrategy(new MockRequest());
        List<Score> expected = new ArrayList<>();
        expected.add(new Score(1234L,4.9));
        expected.add(new Score(5678L,0.0));

        List<Score> actualResult = simService.getMostSimilar("irrelevant");
        Assertions.assertEquals(expected, actualResult);

    }
}