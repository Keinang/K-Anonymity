package App.Utils;

import App.Model.DataSetModel;
import App.Model.Edge;
import App.Model.Vertex;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    private static Logger logger = Logger.getLogger(DemoDataCreator.class);

    public static final int VERTICES_SIZE = 5000;
    public static final double EDGES_SIZE = (VERTICES_SIZE * 1.3);

    private static final Random position = new Random();

    public Set<Edge> createDemoEdges(Set<Vertex> verticesSet) {
        Set<Edge> edges = new HashSet<>();
        Vertex[] vertices = verticesSet.toArray(new Vertex[verticesSet.size()]);
        int randomSize = verticesSet.size();
        for (int i = 0; i < EDGES_SIZE; i++) {
            int xIdx = position.nextInt(randomSize);
            int yIdx = position.nextInt(randomSize);

            edges.add(new Edge(vertices[xIdx], vertices[yIdx]));
        }

        return edges;
    }

    public Set<Vertex> createDemoVertices() {
        Set<Vertex> result = new HashSet<>();

        for (int i = 0; i < VERTICES_SIZE; i++) {
            result.add(new Vertex(String.valueOf(i)));
        }
        return result;
    }

    public DataSetModel getDataSetToModel() {
        DataSetModel model = new DataSetModel();
        model.setVertices(createDemoVertices());
        model.setEdges(createDemoEdges(model.getVertices()));
        model.setTitle("Demo chart");
        return model;
    }
}
