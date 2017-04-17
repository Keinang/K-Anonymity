package App.Model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class Graph implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2193295438102861321L;
	private Set<Edge> edges;
    private List<Vertex> vertices;
    private Map<Vertex, Set<Vertex>> vertexToNeighbors;
    // relevant for K-Symmetry
    private List<List<Vertex>> partitions;

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

    public Set<Edge> getEdges() {
        return edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setPartitions(List<List<Vertex>> partitions) {
        this.partitions = partitions;
    }

    public List<List<Vertex>> getPartitions() {
        return partitions;
    }
}
