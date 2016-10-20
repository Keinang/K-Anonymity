package App;

import App.View.AppFrame;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class Main {
    @Autowired
    private AppFrame appFrame;

    public Main() {
        BasicConfigurator.configure();
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.appFrame.initUIComponents();
        main.appFrame.setVisible(true);
    }
}
