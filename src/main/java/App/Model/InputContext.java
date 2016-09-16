package App.Model;

/**
 * Created by Keinan.Gilad on 9/14/2016.
 */
public class InputContext {

    private String algoTypeCommand;
    private String KTypeCommand;
    private String dataSetCommand;

    public String getAlgoTypeCommand() {
        return algoTypeCommand;
    }

    public String getKTypeCommand() {
        return KTypeCommand;
    }

    public String getDataSetCommand() {
        return dataSetCommand;
    }

    public void setAlgoTypeCommand(String algoTypeCommand) {
        this.algoTypeCommand = algoTypeCommand;
    }

    public void setKTypeCommand(String KTypeCommand) {
        this.KTypeCommand = KTypeCommand;
    }

    public void setDataSetCommand(String dataSetCommand) {
        this.dataSetCommand = dataSetCommand;
    }
}
