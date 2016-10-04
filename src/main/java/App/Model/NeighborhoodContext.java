package App.Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Created by Keinan.Gilad on 10/3/2016.
 */
public class NeighborhoodContext implements Comparable<NeighborhoodContext> {
    private Vertex vertex;
    private Set<Vertex> neighborsVertices;
    private List<Edge> neighborsEdges;
    private boolean isAnonymized;

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public Set<Vertex> getNeighborsVertices() {
        return neighborsVertices;
    }

    public void setNeighborsVertices(Set<Vertex> neighborsVertices) {
        this.neighborsVertices = neighborsVertices;
    }

    public List<Edge> getNeighborsEdges() {
        return neighborsEdges;
    }

    public void setNeighborsEdges(List<Edge> neighborsEdges) {
        this.neighborsEdges = neighborsEdges;
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
            int otherVerticesSize = o.getNeighborsVertices().size();
            int thisVerticesSize = neighborsVertices.size();

            if (otherVerticesSize == thisVerticesSize) {
                int otherEdgesSize = o.getNeighborsEdges().size();
                int thisEdgesSize = this.neighborsEdges.size();

                return new Integer(otherEdgesSize).compareTo(Integer.valueOf(thisEdgesSize));
            } else {
                return new Integer(otherVerticesSize).compareTo(Integer.valueOf(thisVerticesSize));
            }

        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("v=%s, V(v)=%s, E(v)=%s\n", vertex, neighborsVertices.size(), neighborsEdges.size());
    }
}
