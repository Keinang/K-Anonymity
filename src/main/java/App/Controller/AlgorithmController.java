package App.Controller;

import App.Model.AlgoType;
import App.Model.Graph;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Keinan.Gilad on 10/20/2016.
 */
public class AlgorithmController {

    @Autowired
    private KDegreeAlgorithm kDegreeAlgorithm;


    public Graph anonymize(String algorithm, Graph originalGraph, Integer k) {
        Graph anonymizeData = null;
        if (AlgoType.KDegree.toString().equals(algorithm)) {
            anonymizeData = kDegreeAlgorithm.anonymize(originalGraph, k);

        }

        return anonymizeData;
    }
}
