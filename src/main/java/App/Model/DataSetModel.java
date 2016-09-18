package App.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class DataSetModel {

    private Set<Edge> edges;
    private Set<Vertex> vertices;
    private String title;

    public DataSetModel() {
        edges = new HashSet<>();
        vertices = new HashSet<>();
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Set<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void addRow(String[] valueRowSplits) {
        Vertex v0 = new Vertex(valueRowSplits[0]);
        Vertex v1 = new Vertex(valueRowSplits[1]);

        vertices.add(v0);
        vertices.add(v1);
        edges.add(new Edge(v0, v1));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
