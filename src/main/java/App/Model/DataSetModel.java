package App.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class DataSetModel {

    private Set<Edge> edges;
    private Set<Vertex> vertices;
    private String title;
    private Map<Vertex, Integer> vertexToDegree;

    public DataSetModel() {
        edges = new HashSet<>();
        vertices = new HashSet<>();
        vertexToDegree = new HashMap<>();
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

        // update degree:
        Integer v0Degree = vertexToDegree.get(v0);
        if (v0Degree == null) {
            v0Degree = 0;
        }
        Integer v1Degree = vertexToDegree.get(v1);
        if (v1Degree == null) {
            v1Degree = 0;
        }
        vertexToDegree.put(v0, v0Degree + 1);
        vertexToDegree.put(v1, v1Degree + 1);
    }

    public Map<Vertex, Integer> getVertexToDegree() {
        return vertexToDegree;
    }

    public void setVertexToDegree(Map<Vertex, Integer> vertexToDegree) {
        this.vertexToDegree = vertexToDegree;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
