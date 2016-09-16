package App.Utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keinan.Gilad on 9/16/2016.
 */
public class FileUtil {
    private Logger logger = Logger.getLogger(FileUtil.class);

    public List<String> loadDataSet(String fileName) {
        List<String> stringsFromFile = new ArrayList<>();
        String str = "";
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));
            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    stringsFromFile.add(str);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e1) {
            logger.error(e1);
        }

        return stringsFromFile;
    }
}
