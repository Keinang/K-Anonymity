package App.Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Keinan.Gilad on 10/3/2016.
 */
public class NeighborhoodContext implements Comparable<NeighborhoodContext> {
    private Vertex vertex;
    private boolean isAnonymized;
    private Double cost;
    private int vertices;
    private int edges;

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public int getEdges() {
        return edges;
    }

    public void setEdges(int edges) {
        this.edges = edges;
    }

    private List<ComponentCode> neighborhoodComponentCode; // NCC for each component

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public boolean isAnonymized() {
        return isAnonymized;
    }

    public void setAnonymized(boolean anonymized) {
        isAnonymized = anonymized;
    }

    @Override
    public int compareTo(@NotNull NeighborhoodContext o) {
        try {
            if (vertices == o.vertices) {
                return new Integer(o.edges).compareTo(Integer.valueOf(this.edges));
            } else {
                return new Integer(o.vertices).compareTo(Integer.valueOf(this.vertices));
            }

        } catch (Exception e) {
            return 0;
        }
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public List<ComponentCode> getNeighborhoodComponentCode() {
        return neighborhoodComponentCode;
    }

    public void setNeighborhoodComponentCode(List<ComponentCode> neighborhoodComponentCode) {
        this.neighborhoodComponentCode = neighborhoodComponentCode;
    }

    @Override
    public String toString() {
        return String.format("v=%s, V(v)=%s, E(v)=%s\n", vertex, this.vertices, this.edges);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getVertex().equals(((NeighborhoodContext) obj).getVertex());
    }
}
