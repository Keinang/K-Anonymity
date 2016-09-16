package App.Controller;

import App.Utils.FileUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Keinan.Gilad on 9/16/2016.
 */
public class DataSetController {
    private static Logger logger = Logger.getLogger(DataSetController.class);

    @Autowired
    private FileUtil fileUtils;
    private List<String> dataSetsNames = Arrays.asList("Facebook circles", "Wikipedia voting", "Twitter circles");

    public DataSetController() {
        initDataSets();
    }

    private void initDataSets() {

    }

    public List<String> getDataSetsNames() {
        return dataSetsNames;
    }
}
