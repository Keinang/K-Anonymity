package App.Controller;

import App.Model.AlgoType;
import App.Model.Graph;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Keinan.Gilad on 10/20/2016.
 */
public class AlgorithmController {

    @Autowired
    private KDegree kDegree;

    @Autowired
    private KSymmetry kSymmetry;

    public Graph anonymize(String algorithm, Graph originalGraph, Integer k) {
        Graph anonymizeData = null;
        if (AlgoType.KDegree.toString().equals(algorithm)) {
            anonymizeData = kDegree.anonymize(originalGraph, k);
        } else if (AlgoType.KSymmetry.toString().equals(algorithm)) {
            anonymizeData = kSymmetry.anonymize(originalGraph, k);
        }

        return anonymizeData;
    }
}
