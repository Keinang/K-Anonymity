package App.Utils;

import App.Model.DegreeContext;
import App.Model.Graph;
import App.Model.Vertex;

import java.util.*;

/**
 * Created by Keinan.Gilad on 10/16/2016.
 */
public class DegreeUtil {

    public static List<DegreeContext> sortByDegree(Graph graph){
        List<DegreeContext> result = new ArrayList<>();

        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();
        Set<Vertex> vertices = vertexToNeighbors.keySet();

        Iterator<Vertex> vertexIterator = vertices.iterator();
        while (vertexIterator.hasNext()) {
            Vertex vertex = vertexIterator.next();
            Set<Vertex> neighbors = vertexToNeighbors.get(vertex);
            DegreeContext vertexDegreeContext = new DegreeContext(vertex, neighbors.size());
            result.add(vertexDegreeContext);
        }

        Collections.sort(result);
        return result;
    }
}
