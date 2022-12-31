package cat.cultura.backend.utils;

import java.util.Map;
import java.util.Objects;

import static java.lang.Double.NaN;

public class Score {

    private Long eventId;

    public Score(Long eventId, Double similarityScore) {
        this.eventId = eventId;
        this.similarityScore = similarityScore;
    }

    public static Score parseScore(Map.Entry<String, Double> item) {
        double score = item.getValue();
        if (Double.isNaN(score))
            score = 0.0;
        return new Score(Long.valueOf(item.getKey()), score);
    }

    public Score(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score score)) return false;
        return eventId.equals(score.eventId) && similarityScore.equals(score.similarityScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, similarityScore);
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
