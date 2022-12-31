package cat.cultura.backend.remoterequests;

import cat.cultura.backend.utils.Score;

import java.io.IOException;
import java.util.List;

public interface SimilarityServiceAdapter {

    List<Score> getMostSimilar(String query) throws IOException;
}
