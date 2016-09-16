package App.Utils;

import App.Model.Edge;
import App.Model.Vertice;
import org.apache.log4j.Logger;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    private static Logger logger = Logger.getLogger(DemoDataCreator.class);
    private static final Random position = new Random();

    public List<Edge> createDemoEdges(List<Vertice> vertices) {
        List<Edge> edges = new ArrayList<Edge>();
        int randomSize = vertices.size();
        for (int i = 0; i < 100; i++) {
            Edge edge = new Edge();
            int xIdx = position.nextInt(randomSize);
            int yIdx = position.nextInt(randomSize);
            edge.setX(vertices.get(xIdx));
            edge.setY(vertices.get(yIdx));
            edges.add(edge);
        }

        return edges;
    }

    public List<Vertice> createDemoVertices() {
        List<Vertice> vertices = new ArrayList<Vertice>();

        for (int i = 0; i < 50; i++) {
            Point point = new Point();
            point.x = position.nextInt(600);
            point.y = position.nextInt(600);
            vertices.add(new Vertice(String.valueOf(i), point));
        }
        return vertices;
    }
}
