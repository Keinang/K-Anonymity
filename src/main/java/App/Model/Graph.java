package App.Model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class Graph implements Serializable {

    private Set<Edge> edges;
    private List<Vertex> vertices;
    private String title;
    private String dataSet;
    private Map<Vertex, Set<Vertex>> vertexToNeighbors;
    private boolean isAnonymized = false;
    private long duration = 0;
    private int edgeAdded;
    private List<List<Vertex>> partitions;
    private int verticesAdded;

    public Graph() {
        edges = new HashSet<>();
        vertices = new ArrayList<>();
        vertexToNeighbors = new HashMap<>();
    }

    public void addRow(String[] valueRowSplits) {
        Vertex v0 = new Vertex(valueRowSplits[0]);
        Vertex v1 = new Vertex(valueRowSplits[1]);

        addVertex(v0);
        addVertex(v1);

        edges.add(new Edge(v0, v1));

        // update degrees:
        updateNeightbors(v0, v1);
    }

    public void addVertex(Vertex v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
        }
    }

    private void updateNeightbors(Vertex v0, Vertex v1) {
        updateNeighbor(v0, v1);
        updateNeighbor(v1, v0);
    }

    private void updateNeighbor(Vertex v0, Vertex v1) {
        Set<Vertex> vertices = vertexToNeighbors.get(v0);
        if (vertices == null) {
            vertices = new HashSet<Vertex>();
        }
        vertices.add(v1);
        vertexToNeighbors.put(v0, vertices);
    }

    public void addEdge(Vertex v0, Vertex v1) {
        edges.add(new Edge(v0, v1));
        addVertex(v0);
        addVertex(v1);
        updateNeightbors(v0, v1);
    }

    public Map<Vertex, Set<Vertex>> getVertexToNeighbors() {
        return vertexToNeighbors;
    }

    public void setVertexToNeighbors(Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        this.vertexToNeighbors = vertexToNeighbors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public boolean isAnonymized() {
        return isAnonymized;
    }

    public void setAnonymized(boolean anonymized) {
        isAnonymized = anonymized;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setEdgeAdded(int edgeAdded) {
        this.edgeAdded = edgeAdded;
    }

    public int getEdgeAdded() {
        return edgeAdded;
    }

    public void setPartitions(List<List<Vertex>> partitions) {
        this.partitions = partitions;
    }

    public List<List<Vertex>> getPartitions() {
        return partitions;
    }

    public void setVerticesAdded(int verticesAdded) {
        this.verticesAdded = verticesAdded;
    }

    public Integer getVerticesAdded() {
        return this.verticesAdded;
    }
}
