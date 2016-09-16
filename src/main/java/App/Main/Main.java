package App.Main;

import App.View.AppFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class Main {
    @Autowired
    private AppFrame appFrame;

    public Main(){
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
