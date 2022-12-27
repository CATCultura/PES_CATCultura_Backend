package cat.cultura.backend.utils;

import groovy.util.MapEntry;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void parseScoreOk() {
        Map.Entry<String,Double> receivedScore = new MapEntry("123",7.9);
        Score expected = new Score();
        expected.setEventId(123L);
        expected.setSimilarityScore(7.9);

        Score result = Score.parseScore(receivedScore);
        Assertions.assertEquals(expected,result);
    }

    @Test
    void parseScoreNotOk() {
        Map.Entry<String,Double> receivedScore = new MapEntry("123",NaN);
        Score expected = new Score();
        expected.setEventId(123L);
        expected.setSimilarityScore(0.0);

        Score result = Score.parseScore(receivedScore);
        Assertions.assertEquals(expected,result);
    }
}