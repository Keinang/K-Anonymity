package App.Utils;

import App.Model.DataSetModel;
import App.Model.Edge;
import App.Model.Vertex;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class DegreeUtil {

    public static Map<Integer, Integer> getDegreeCounters(DataSetModel model) {
        Map<Integer, Integer> degreeToCounter = new HashMap<>();
        for (Vertex v : model.getVertices()) {
            Integer degree = findVertexDegree(v, model);
            if (degreeToCounter.containsKey(degree)) {
                Integer lastCount = degreeToCounter.get(degree);
                // inc by one
                degreeToCounter.put(degree, lastCount + 1);
            } else {
                // first time to enter
                degreeToCounter.put(degree, 1);
            }
        }
        return degreeToCounter;
    }

    private static Integer findVertexDegree(Vertex v, DataSetModel model) {
        int degree = 0;
        for (Edge e : model.getEdges()) {
            if (e.getV1().equals(v) || e.getV0().equals(v)) {
                degree++;
            }
        }

        return degree;
    }
}
