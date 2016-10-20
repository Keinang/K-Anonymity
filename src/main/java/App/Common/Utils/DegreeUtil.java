package App.Common.Utils;

import App.Model.DegreeContext;
import App.Model.Graph;
import App.Model.Vertex;
import org.springframework.util.CollectionUtils;

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

    public static boolean isEdgeBetween(Vertex v1, Vertex v2, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        return vertexToNeighbors.get(v1).contains(v2); //no need || vertexToNeighbors.get(v2).contains(v1);
    }

    public static int indexOf(Vertex v, Set<Vertex> vertices){
        return CollectionUtils.arrayToList(vertices.toArray()).indexOf(v);
    }

    public static Vertex getVertexInIndex(int idx, Set<Vertex> vertices){
        return (Vertex) CollectionUtils.arrayToList(vertices.toArray()).get(idx);
    }
}
