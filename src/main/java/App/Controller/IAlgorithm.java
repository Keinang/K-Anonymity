package App.Controller;

import App.Model.DataSetModel;

/**
 * Created by Keinan.Gilad on 10/1/2016.
 */
public interface IAlgorithm {

    DataSetModel annonymize(DataSetModel originalGraph, Integer k);
}
