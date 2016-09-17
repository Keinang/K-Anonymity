package App.Utils;

import App.Model.Edge;
import App.Model.Vertice;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    public static final int VERTICES_SIZE = 5000;
    public static final double EDGES_SIZE = (VERTICES_SIZE * 1.3);
    private static Logger logger = Logger.getLogger(DemoDataCreator.class);
    private static final Random position = new Random();

    public List<Edge> createDemoEdges(HashMap<String, Vertice> vertices) {
        Vertice[] verticesArr = vertices.values().toArray(new Vertice[vertices.size()]);

        List<Edge> edges = new ArrayList<Edge>();
        int randomSize = vertices.size();
        for (int i = 0; i < EDGES_SIZE; i++) {
            int xIdx = position.nextInt(randomSize);
            int yIdx = position.nextInt(randomSize);

            edges.add(new Edge(verticesArr[xIdx], verticesArr[yIdx]));
        }

        return edges;
    }

    public HashMap<String, Vertice> createDemoVertices() {
        HashMap<String, Vertice> vertices = new HashMap<String, Vertice>();

        for (int i = 0; i < VERTICES_SIZE; i++) {
            Point point = new Point();
            point.x = position.nextInt(600);
            point.y = position.nextInt(600);
            String name = String.valueOf(i);
            vertices.put(name, new Vertice(name, point));
        }
        return vertices;
    }
}