package App.lib.jNauty;

import App.Common.Utils.DegreeUtil;
import App.Model.Edge;
import App.Model.Vertex;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BinaryRepresentation<V extends Vertex, E extends Edge> {

    public static String binaryRepresenatation(List<Vertex> verticeList, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        StringBuilder representation = new StringBuilder();
        for (int i = 0; i < verticeList.size(); i++) {
            Vertex v1 = verticeList.get(i);
            for (int j = i + 1; j < verticeList.size(); j++) {
                Vertex v2 = verticeList.get(j);
                if (DegreeUtil.isEdgeBetween(v1, v2, vertexToNeighbors)) {
                    representation.append("1");
                } else {
                    representation.append("0");
                }
            }
        }
        return representation.toString();
    }
}
