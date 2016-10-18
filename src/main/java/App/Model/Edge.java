package App.Model;

import java.io.Serializable;

/**
 * Created by Keinan.Gilad on 9/17/2016.
 */
public class Edge implements Serializable {

    private Vertex v0;
    private Vertex v1;
    private boolean isForward;//used for K-Neighborhood algorithm.

    public Edge(Vertex v0, Vertex v1) {
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

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Edge && (((Edge) obj).getV0().equals(this.v0) && ((Edge) obj).getV1().equals(this.v1)
                || ((Edge) obj).getV0().equals(this.v1) && ((Edge) obj).getV1().equals(this.v0));
    }

    @Override
    public String toString(){
        return String.format("%s-%s", this.getV0(), this.getV1());
    }

    @Override
    public int hashCode() {
        return this.getV0().hashCode() + this.getV1().hashCode();
    }

    public boolean isForward() {
        return isForward;
    }

    public void setForward(boolean forward) {
        isForward = forward;
    }
}
