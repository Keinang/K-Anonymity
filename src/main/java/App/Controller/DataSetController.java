package App.Controller;

import App.Model.Edge;
import App.Model.Vertice;
import App.Utils.FileUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Keinan.Gilad on 9/16/2016.
 */
public class DataSetController {
    private static Logger logger = Logger.getLogger(DataSetController.class);

    public static final String FACEBOOK_CIRCLES = "Facebook circles";
    public static final String WIKIPEDIA_VOTING = "Wikipedia voting";
    public static final String TWITTER_CIRCLES = "Twitter circles";
    public static final String FACEBOOK_CIRCLES_FILE = "/facebook_combined.txt";
    public static final String WIKI_VOTE_FILE = "/wiki-Vote.txt";
    public static final String TWITTER_COMBINED_FILE = "/twitter_combined.txt";

    @Autowired
    private FileUtil fileUtils;
    private static final Random position = new Random();
    private List<String> dataSetsNames = Arrays.asList(FACEBOOK_CIRCLES, WIKIPEDIA_VOTING, TWITTER_CIRCLES);
    private HashMap<String, String> dataSetNameToFileName;
    private HashMap<String, HashMap<String, Vertice>> dataSetToVertices;
    private HashMap<String, List<Edge>> dataSetToEdges;
    private HashMap<String, Integer> dataSetToProgress;

    public DataSetController() {
        dataSetNameToFileName = new HashMap<>();
        dataSetNameToFileName.put(FACEBOOK_CIRCLES, FACEBOOK_CIRCLES_FILE);
        dataSetNameToFileName.put(WIKIPEDIA_VOTING, WIKI_VOTE_FILE);
        dataSetNameToFileName.put(TWITTER_CIRCLES, TWITTER_COMBINED_FILE);
        dataSetToVertices = new HashMap<>();
        dataSetToEdges = new HashMap<>();
        dataSetToProgress = new HashMap<>();
    }

    public List<String> getDataSetsNames() {
        return dataSetsNames;
    }

    public void loadDataSet(String dataSet) {
        dataSetToProgress.put(dataSet, 0);

        String fileName = dataSetNameToFileName.get(dataSet);
        List<String> values = fileUtils.loadDataSet(fileName);

        HashMap<String, Vertice> verticeNameToVertice = new HashMap<>();
        List<Edge> edges = new ArrayList<>();
        logger.debug("Start creating data set model for:" + dataSet);

        int size = values.size();
        for (int i = 0; i < size; i++) {
            String valueI = values.get(i);
            // split by spaces
            String[] valueISplits = valueI.split("\\s+");
            // taking only the first two (as this is in my data sets)

            Vertice v0 = createVertice(valueISplits[0], verticeNameToVertice);
            Vertice v1 = createVertice(valueISplits[1], verticeNameToVertice);

            edges.add(new Edge(v0.getLocation(), v1.getLocation()));

            // done iteration
            int progress = ((i * 100) / size) + 1;
            dataSetToProgress.put(dataSet, progress);
            dataSetToVertices.put(dataSet, verticeNameToVertice);
            dataSetToEdges.put(dataSet, edges);
            //logger.debug("DataSet: " + dataSet + " Progress:" + progress);
        }
        dataSetToProgress.put(dataSet, 100);
        dataSetToVertices.put(dataSet, verticeNameToVertice);
        dataSetToEdges.put(dataSet, edges);
        logger.debug("Done creating data set model for:" + dataSet);
    }

    private Vertice createVertice(String name, HashMap<String, Vertice> vertices) {
        if (!vertices.containsKey(name)){

            Point point = new Point();
            point.x = position.nextInt(600);
            point.y = position.nextInt(600);
            Vertice v = new Vertice(name, point);
            vertices.put(name, v);
        }

        return vertices.get(name);
    }

    public int getProgress(String dataSet) {
        return dataSetToProgress.get(dataSet);
    }

    public HashMap<String, Vertice> getVerticesByDataSet(String dataSet) {
        return dataSetToVertices.get(dataSet);
    }

    public List<Edge> getEdgesByDataSet(String dataSet) {
        return dataSetToEdges.get(dataSet);
    }
}