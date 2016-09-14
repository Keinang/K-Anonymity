import UI.AppFrame;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    private static JFrame myFrame = null;

    public static void main(String[] args)  {
        BasicConfigurator.configure();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                myFrame = new AppFrame();
                myFrame.setVisible(true);
            }
        });
    }
}
