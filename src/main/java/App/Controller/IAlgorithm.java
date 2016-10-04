package App.Controller;

import App.Model.Graph;

/**
 * Created by Keinan.Gilad on 10/1/2016.
 */
public interface IAlgorithm {

    Graph anonymize(Graph originalGraph, Integer k);
}
