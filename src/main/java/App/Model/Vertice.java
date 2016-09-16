package App.Model;

import java.awt.*;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class Vertice extends Point {
    private String name;

    public Vertice(String name, Point point) {
        this.name = name;
        this.x = point.x;
        this.y = point.y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
