package App.Utils;

import App.Model.Graph;
import App.Model.Vertex;

import java.util.Random;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    public static final int VERTICES_SIZE = 10; // approx.
    public static final double EDGES_SIZE = VERTICES_SIZE * 0.7;

    private static final Random position = new Random();

    public static Graph generateRandomGraph() {
        Graph model = new Graph();
        for (int i = 0; i < EDGES_SIZE; i++) {
            int xIdx = position.nextInt(VERTICES_SIZE);
            int yIdx = position.nextInt(VERTICES_SIZE);
            model.addRow(new String[]{String.valueOf(xIdx), String.valueOf(yIdx)});
        }
        model.setTitle("Demo Data");
        return model;
    }

    public static Graph generateGraphSymmetry() {
        Graph model = new Graph();
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");
        Vertex v5 = new Vertex("5");
        Vertex v6 = new Vertex("6");
        Vertex v7 = new Vertex("7");
        Vertex v8 = new Vertex("8");

        model.addRow(new String[]{String.valueOf(v1), String.valueOf(v3)});
        model.addRow(new String[]{String.valueOf(v2), String.valueOf(v3)});
        model.addRow(new String[]{String.valueOf(v3), String.valueOf(v4)});
        model.addRow(new String[]{String.valueOf(v3), String.valueOf(v5)});
        model.addRow(new String[]{String.valueOf(v4), String.valueOf(v5)});
        model.addRow(new String[]{String.valueOf(v4), String.valueOf(v6)});
        model.addRow(new String[]{String.valueOf(v5), String.valueOf(v7)});
        model.addRow(new String[]{String.valueOf(v6), String.valueOf(v8)});
        model.addRow(new String[]{String.valueOf(v7), String.valueOf(v8)});
        return model;
    }
}
