package App.Utils;

import edu.uci.ics.jung.algorithms.blockmodel.GraphCollapser;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    private static Logger logger = Logger.getLogger(DemoDataCreator.class);

    public static final int VERTICES_SIZE = 5000;
    public static final double EDGES_SIZE = (VERTICES_SIZE * 1.3);

    private static final Random position = new Random();

    public List<Edge> createDemoEdges(Map<String, Vertex> verticesMap) {
        List<Edge> edges = new ArrayList<>();
        Collection<Vertex> verticesValues = verticesMap.values();
        Vertex[] vertices = verticesValues.toArray(new Vertex[verticesValues.size()]);
        int randomSize = verticesValues.size();
        for (int i = 0; i < EDGES_SIZE; i++) {
            int xIdx = position.nextInt(randomSize);
            int yIdx = position.nextInt(randomSize);

            edges.add(new GraphCollapser.UndirectedCollapsedEdge(vertices[xIdx], vertices[yIdx], null));
        }

        return edges;
    }

    public Map<String, Vertex> createDemoVertices() {
        Map<String, Vertex> result = new HashMap<String, Vertex>();

        for (int i = 0; i < VERTICES_SIZE; i++) {
            result.put(String.valueOf(i), new SparseVertex());
        }
        return result;
    }
}
