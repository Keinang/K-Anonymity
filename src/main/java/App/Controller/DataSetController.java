package App.Controller;

import App.Model.EdgeWrapper;
import App.Utils.FileUtil;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private List<String> dataSetsNames = Arrays.asList(FACEBOOK_CIRCLES, WIKIPEDIA_VOTING);//, TWITTER_CIRCLES);
    private HashMap<String, String> dataSetNameToFileName;
    private HashMap<String, HashMap<String, Vertex>> dataSetToVertices;
    private HashMap<String, List<EdgeWrapper>> dataSetToEdges;
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

        HashMap<String, Vertex> verticeNameToVertice = new HashMap<>();
        List<EdgeWrapper> edges = new ArrayList<>();
        logger.debug("Start load DataSet:" + dataSet);

        int size = values.size();
        for (int i = 0; i < size; i++) {
            String valueI = values.get(i);
            // split by spaces
            String[] valueISplits = valueI.split("\\s+");
            // taking only the first two (as this is in my data sets)
            if (valueISplits[0].startsWith("#")) {
                continue;
            }

            Vertex v0 = createVertice(valueISplits[0], verticeNameToVertice);
            Vertex v1 = createVertice(valueISplits[1], verticeNameToVertice);
            edges.add(new EdgeWrapper(v0, v1));

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
        logger.debug("Done load DataSet:" + dataSet);
    }

    private Vertex createVertice(String name, HashMap<String, Vertex> vertices) {
        if (!vertices.containsKey(name)) {
            Vertex v = new SparseVertex();
            vertices.put(name, v);
        }

        return vertices.get(name);
    }

    public int getProgress(String dataSet) {
        return dataSetToProgress.get(dataSet);
    }

    public HashMap<String, Vertex> getVerticesByDataSet(String dataSet) {
        return dataSetToVertices.get(dataSet);
    }

    public List<EdgeWrapper> getEdgesByDataSet(String dataSet) {
        return dataSetToEdges.get(dataSet);
    }
}