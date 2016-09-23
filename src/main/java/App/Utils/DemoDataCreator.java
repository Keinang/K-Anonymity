package App.Utils;

import App.Model.DataSetModel;

import java.util.Random;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class DemoDataCreator {
    public static final int VERTICES_SIZE = 5000; // approx.
    public static final double EDGES_SIZE = VERTICES_SIZE * 1.3;

    private static final Random position = new Random();

    public DataSetModel getDataSetToModel() {
        DataSetModel model = new DataSetModel();
        for (int i = 0; i < EDGES_SIZE; i++) {
            int xIdx = position.nextInt(VERTICES_SIZE);
            int yIdx = position.nextInt(VERTICES_SIZE);
            model.addRow(new String[]{String.valueOf(xIdx), String.valueOf(yIdx)});
        }
        model.setTitle("Demo Data");
        return model;
    }
}
