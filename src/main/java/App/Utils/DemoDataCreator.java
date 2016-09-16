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
    private static Logger logger = Logger.getLogger(DemoDataCreator.class);
    private static final Random position = new Random();

    public List<Edge> createDemoEdges(HashMap<String, Vertice> vertices) {
        Vertice[] verticesArr = (Vertice[]) vertices.values().toArray();
        List<Edge> edges = new ArrayList<Edge>();
        int randomSize = vertices.size();
        for (int i = 0; i < 100; i++) {
            int xIdx = position.nextInt(randomSize);
            int yIdx = position.nextInt(randomSize);
            edges.add(new Edge(verticesArr[xIdx], verticesArr[yIdx]));
        }

        return edges;
    }

    public HashMap<String, Vertice> createDemoVertices() {
        HashMap<String, Vertice> vertices = new HashMap<String, Vertice>();

        for (int i = 0; i < 50; i++) {
            Point point = new Point();
            point.x = position.nextInt(600);
            point.y = position.nextInt(600);
            String name = String.valueOf(i);
            vertices.put(name, new Vertice(name, point));
        }
        return vertices;
    }
}
