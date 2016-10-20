package App.lib.jNauty;

import App.Model.Edge;
import App.Model.Graph;
import App.Model.Vertex;
import App.Common.Utils.DegreeUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BinaryRepresentation<V extends Vertex, E extends Edge> {

    private Graph graph;

    public BinaryRepresentation(Graph graph) {
        this.graph = graph;
    }

    public String binaryRepresenatation(List<Vertex> verticeList) {
        String representation = "";
        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();
        for (int i = 0; i < verticeList.size(); i++) {
            Vertex v1 = verticeList.get(i);
            for (int j = i + 1; j < verticeList.size(); j++) {
                Vertex v2 = verticeList.get(j);
                if (DegreeUtil.isEdgeBetween(v1, v2, vertexToNeighbors))
                    representation += "1";
                else
                    representation += "0";
            }
        }
        return representation;
    }
}
