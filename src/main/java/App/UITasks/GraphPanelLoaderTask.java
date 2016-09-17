package App.UITasks;

import App.Controller.DataSetController;
import App.Model.EdgeWrapper;
import App.View.jungGraphPanel;
import edu.uci.ics.jung.algorithms.blockmodel.GraphCollapser;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                    jungGraphPanel originalGraph = new jungGraphPanel();
                    HashMap<String, Vertex> vertices = dataSetController.getVerticesByDataSet(dataSet);
                    originalGraph.addVertexes(vertices);

                    List<EdgeWrapper> edges = dataSetController.getEdgesByDataSet(dataSet);
                    List<Edge> graphEdges = new ArrayList<>();
                    for (EdgeWrapper edge : edges) {
                        graphEdges.add(new GraphCollapser.UndirectedCollapsedEdge(edge.getV0(), edge.getV1(), null));
                    }

                    originalGraph.addEdges(graphEdges);
                    originalGraph.addTable();

                    firePropertyChange("done", null, originalGraph);
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
