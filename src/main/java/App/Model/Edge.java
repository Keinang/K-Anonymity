package App.Model;

import java.awt.*;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class Edge {
    private Point x;
    private Point y;

    public Edge(Point x, Point y){
        this.x = x;
        this.y = y;
    }

    public Point getX() {
        return x;
    }

    public void setX(Point x) {
        this.x = x;
    }

    public Point getY() {
        return y;
    }

    public void setY(Point y) {
        this.y = y;
    }
}
