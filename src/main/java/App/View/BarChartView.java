package App.View;

import App.Model.DataSetModel;
import App.Utils.DegreeUtil;
import App.Utils.DemoDataCreator;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.*;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class BarChartView {
    private static Logger logger = Logger.getLogger(BarChartView.class);
    public static final String ORIGINAL_CHART = "Original Chart";
    public static final String VERTICES = "Vertices";
    public static final String DEGREE = "Degree";
    private BarChart<Number, Number> chart;

    public BarChartView(DataSetModel model) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(DEGREE); // 0.. max
        yAxis.setLabel(VERTICES); // counters
        chart = new BarChart(xAxis, yAxis);
        chart.setTitle(ORIGINAL_CHART + " for " + model.getTitle());

        // create new series:
        XYChart.Series series1 = new XYChart.Series();
        series1.setName(model.getTitle());
        ObservableList data = series1.getData();

        data.addAll(getData(model));

        // adding the series to the chart:
        chart.getData().addAll(series1);
    }

    /**
     * Calculate for xAxis and yAxis
     * xAxis = degree 0..Max
     * yAxis = counter of that degree
     *
     * @param model
     * @return collection of XYChart data
     */
    private Collection<XYChart.Data<String, Integer>> getData(DataSetModel model) {
        Collection<XYChart.Data<String, Integer>> chartModel = new ArrayList<>();

        Map<Integer, Integer> degreeCounters = DegreeUtil.getDegreeCounters(model);
        Set<Integer> degreeKeys = degreeCounters.keySet();
        Iterator<Integer> iterator = degreeKeys.iterator();
        while (iterator.hasNext()) {
            Integer degree = iterator.next();
            chartModel.add(new XYChart.Data<>(degree.toString(), degreeCounters.get(degree)));
        }
        return chartModel;
    }

    public BarChart<Number, Number> getChart() {
        return chart;
    }

    public void setChart(BarChart<Number, Number> chart) {
        this.chart = chart;
    }

    public static void main(String[] args) {
        final DemoDataCreator demoDataCreatorLocal = new DemoDataCreator();

        //create a window to display...
        JFrame jf = new JFrame("basic graph");
        final JFXPanel fxPanel = new JFXPanel();
        jf.add(fxPanel);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DataSetModel dataSetToModel = demoDataCreatorLocal.getDataSetToModel();
                BarChartView chart = new BarChartView(dataSetToModel);
                fxPanel.setScene(new Scene(chart.chart));
            }
        });

        //set some size
        jf.setSize(1200, 1200);

        //do something when click on x
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make sure everything fits...
        jf.pack();
        //make it show up...
        jf.setVisible(true);
    }
}
