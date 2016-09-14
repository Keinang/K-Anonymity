package Service;

import Model.InputContext;
import Model.ResultContext;
import UI.AppFrame;
import org.apache.log4j.Logger;

/**
 * Created by Keinan.Gilad on 9/14/2016.
 */
public class ExecuteManager {
    private static Logger logger = Logger.getLogger(ExecuteManager.class);
    public ResultContext execute(InputContext context){
        ResultContext resultContext = new ResultContext();
        // TODO - create thread and execute the algorithm.
        return resultContext;
    }
}
