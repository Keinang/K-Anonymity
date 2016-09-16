package App.Controller;

import App.Model.InputContext;
import App.Model.ResultContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by Keinan.Gilad on 9/14/2016.
 */
public class ExecuteController {
    private static Logger logger = Logger.getLogger(ExecuteController.class);

    public ResultContext execute(InputContext context) {
        ResultContext resultContext = new ResultContext();
        // TODO - create thread and execute the algorithm.
        return resultContext;
    }
}
