package App.Controller;

import App.Common.Utils.FileUtil;
import App.Model.Graph;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by Keinan.Gilad on 9/16/2016.
 */
public class DataSetController {
    private static Logger logger = Logger.getLogger(DataSetController.class);

    public static final String FACEBOOK_CIRCLES = "Facebook circles";
    public static final String FACEBOOK_CIRCLES_FILE = "/DataSets/facebook_combined.txt";
    public static final String WIKIPEDIA_VOTING = "Wikipedia voting";
    public static final String WIKI_VOTE_FILE = "/DataSets/wiki-Vote.txt";
    public static final String TWITTER_CIRCLES = "Twitter circles";
    public static final String TWITTER_COMBINED_FILE = "/DataSets/twitter_combined.txt";
    public static final String ARXIV = "Arxiv Collaboration Network";
    public static final String ARXIV_FILE = "/DataSets/Arxiv_collaboration_network.txt";

    @Autowired
    private FileUtil fileUtils;
    private List<String> dataSetsNames = new ArrayList<>();
    private HashMap<String, String> dataSetNameToFileName = new HashMap<>();
    private HashMap<String, Graph> dataSetToModel = new HashMap<>();
    private HashMap<String, Integer> dataSetToProgress = new HashMap<>();

    public DataSetController() {
        addDataSets();
    }

    private void addDataSets() {
        dataSetsNames.add(FACEBOOK_CIRCLES);
        dataSetNameToFileName.put(FACEBOOK_CIRCLES, FACEBOOK_CIRCLES_FILE);

        dataSetsNames.add(ARXIV);
        dataSetNameToFileName.put(ARXIV, ARXIV_FILE);

        dataSetsNames.add(WIKIPEDIA_VOTING);
        dataSetNameToFileName.put(WIKIPEDIA_VOTING, WIKI_VOTE_FILE);

        //dataSetsNames.add(TWITTER_CIRCLES);
        //dataSetNameToFileName.put(TWITTER_CIRCLES, TWITTER_COMBINED_FILE);
    }

    public List<String> getDataSetsNames() {
        return dataSetsNames;
    }

    public void loadDataSet(String dataSet) {
        logger.debug("Start load DataSet:" + dataSet);

        dataSetToProgress.put(dataSet, 0);
        String fileName = dataSetNameToFileName.get(dataSet);
        List<String> values = fileUtils.loadDataSet(fileName);
        int size = values.size();
        Graph model = new Graph();
        model.setDataSet(dataSet);
        model.setTitle("Original " + dataSet);
        for (int i = 0; i < size; i++) {
            String valueRow = values.get(i);
            // split by spaces
            String[] valueRowSplits = valueRow.split("\\s+");
            // taking only the first two (as this is in my data sets)
            if (valueRowSplits[0].startsWith("#")) {
                continue;
            }

            model.addRow(valueRowSplits);

            // done iteration
            int progress = ((i * 100) / size) + 1;
            dataSetToProgress.put(dataSet, progress);
            //logger.debug("DataSet: " + dataSet + " Progress:" + progress);
        }
        dataSetToProgress.put(dataSet, 100);
        dataSetToModel.put(dataSet, model);
        logger.debug("Done load DataSet:" + dataSet);
    }

    public int getProgress(String dataSet) {
        return dataSetToProgress.get(dataSet);
    }

    public Graph getDataSetToModel(String dataSet) {
        return dataSetToModel.get(dataSet);
    }

    public static Map<Integer, Integer> getDegreeFreq(Collection<Integer> allDegreesWithDuplicates) {
        Iterator<Integer> allDegreesWithDuplicatesIterator = allDegreesWithDuplicates.iterator();
        Set<Integer> degreesSet = new HashSet<>();
        Map<Integer, Integer> degreeToCount = new HashMap<>();
        while (allDegreesWithDuplicatesIterator.hasNext()) {
            Integer degree = allDegreesWithDuplicatesIterator.next();
            if (!degreesSet.contains(degree)) {
                degreesSet.add(degree);
                int frequency = Collections.frequency(allDegreesWithDuplicates, degree);
                degreeToCount.put(degree, frequency);
            }
        }
        return degreeToCount;
    }
}