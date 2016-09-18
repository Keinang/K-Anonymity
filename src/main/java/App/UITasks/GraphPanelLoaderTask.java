package App.UITasks;

import App.Controller.DataSetController;
import App.Model.DataSetModel;
import App.View.BarChartView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Keinan.Gilad on 9/16/2016.
 */
public class GraphPanelLoaderTask extends SwingWorker<Void, Void> {
    private static Logger logger = Logger.getLogger(GraphPanelLoaderTask.class);

    private String dataSet;
    private DataSetController dataSetController;

    public GraphPanelLoaderTask(String dataSet, DataSetController dataSetController) {
        this.dataSet = dataSet;
        this.dataSetController = dataSetController;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            final String dataSet = this.dataSet;

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    final JFXPanel[] jfxPanel = {null};

                    final CountDownLatch latch = new CountDownLatch(1);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            jfxPanel[0] = new JFXPanel();// initializes JavaFX environment

                            DataSetModel dataSetToModel = dataSetController.getDataSetToModel(dataSet);
                            BarChartView chart = new BarChartView(dataSetToModel);
                            jfxPanel[0].setScene(new Scene(chart.getChart()));
                            firePropertyChange("done", null, jfxPanel[0]);
                            latch.countDown();
                        }
                    });
                }
            });

            thread.start();

        } catch (Exception e) {
            logger.error(e);
        }

        return null;
    }

    @Override
    protected void done() {
        super.done();

    }

    public String getDataSetName() {
        return dataSet;
    }
}
