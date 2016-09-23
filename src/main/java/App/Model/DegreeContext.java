package App.Model;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Keinan.Gilad on 9/23/2016.
 */
public class DegreeContext implements Comparable<DegreeContext> {
    private int degree;
    private Vertex vertex;

    public DegreeContext(Vertex vertex, int degree) {
        this.vertex = vertex;
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public int compareTo(@NotNull DegreeContext o) {
        return new Integer(o.getDegree()).compareTo(this.getDegree());
    }

    @Override
    public String toString() {
        return String.valueOf(this.getDegree());
    }
}
