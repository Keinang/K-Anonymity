package App.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

/**
 * Created by Keinan.Gilad on 9/16/2016.
 */
public class DataSetLoaderTask extends SwingWorker<Void, Integer> {
    private static Logger logger = Logger.getLogger(DataSetLoaderTask.class);

    private String dataSet;
    private DataSetController dataSetController;

    public DataSetLoaderTask(String dataSet, DataSetController dataSetController) {
        this.dataSet = dataSet;
        this.dataSetController = dataSetController;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            final String dataSet = this.dataSet;
            logger.info("Starting to load dataset:" + this.dataSet);
            setProgress(0);

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    dataSetController.loadDataSet(dataSet);
                }
            });

            thread.start();

            int progress = 0;
            // Initialize progress property.

            while (progress < 100) {
                // Sleep for up to one second.
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignore) {
                }
                // get current progress.
                progress = dataSetController.getProgress(dataSet);
                setProgress(progress);
            }

        } catch (Exception e) {
            logger.error(e);
        }

        return null;
    }

    public String getDataSetName() {
        return dataSet;
    }
}
