package App.Model;

import edu.uci.ics.jung.graph.Vertex;

/**
 * Created by Keinan.Gilad on 9/17/2016.
 */
public class EdgeWrapper {

    private Vertex v0;
    private Vertex v1;

    public EdgeWrapper(Vertex v0, Vertex v1) {
        this.v0 = v0;
        this.v1 = v1;
    }

    public Vertex getV0() {
        return v0;
    }

    public void setV0(Vertex v0) {
        this.v0 = v0;
    }

    public Vertex getV1() {
        return v1;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }
}
