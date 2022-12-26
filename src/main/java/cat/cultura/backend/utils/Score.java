package cat.cultura.backend.utils;

import java.util.Map;

public class Score {

    private Long eventId;

    public Score(Long eventId, Double similarityScore) {
        this.eventId = eventId;
        this.similarityScore = similarityScore;
    }

    public static Score parseScore(Map.Entry<String, Double> item) {
        return new Score(Long.valueOf(item.getKey()), item.getValue());
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    private Double similarityScore;


}
