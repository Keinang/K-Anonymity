package App.Utils;

import App.Model.Graph;

import java.util.Random;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    public static final int VERTICES_SIZE = 10; // approx.
    public static final double EDGES_SIZE = VERTICES_SIZE * 0.7;

    private static final Random position = new Random();

    public Graph generateGraph() {
        Graph model = new Graph();
        for (int i = 0; i < EDGES_SIZE; i++) {
            int xIdx = position.nextInt(VERTICES_SIZE);
            int yIdx = position.nextInt(VERTICES_SIZE);
            model.addRow(new String[]{String.valueOf(xIdx), String.valueOf(yIdx)});
        }
        model.setTitle("Demo Data");
        return model;
    }

    public Graph generateGraphT2() {
        Graph model = new Graph();
        model.addRow(new String[]{"0", "1"});
        model.setTitle("T2");
        return model;
    }

    public Graph generateGraphT3() {
        Graph model = new Graph();
        model.addRow(new String[]{"0", "1"});
        model.addRow(new String[]{"0", "2"});
        model.addRow(new String[]{"1", "2"});

        model.setTitle("T3");
        return model;
    }

    public Graph generateGraphT4() {
        Graph model = new Graph();
        model.addRow(new String[]{"0", "1"});
        model.addRow(new String[]{"0", "2"});
        model.addRow(new String[]{"0", "3"});
        model.addRow(new String[]{"1", "2"});
        model.addRow(new String[]{"1", "3"});
        model.addRow(new String[]{"2", "3"});

        model.setTitle("T4");
        return model;
    }

    public Graph generateGraphGSpan2b() {
        Graph model = new Graph();
        model.addRow(new String[]{"0", "1"});
        model.addRow(new String[]{"1", "2"});
        model.addRow(new String[]{"2", "3"});
        model.addRow(new String[]{"1", "4"});
        model.addRow(new String[]{"2", "0"});
        model.addRow(new String[]{"1", "3"});

        model.setTitle("T4");
        return model;
    }
}
