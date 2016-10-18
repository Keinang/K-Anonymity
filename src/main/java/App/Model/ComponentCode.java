package App.Model;

import App.Utils.DFSCodeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keinan.Gilad on 10/16/2016.
 */
public class ComponentCode implements Comparable<ComponentCode> {
    private Graph graph;
    private List<Edge> code;
    private List<Vertex> bfsRun;

    public ComponentCode() {
        this.graph = new Graph();
        code = new ArrayList<>();
        bfsRun = new ArrayList<>();
    }

    public List<Edge> getCode() {
        return code;
    }

    public void setCode(List<Edge> code) {
        this.code = code;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<Vertex> getBfsRun() {
        return bfsRun;
    }

    public void setBfsRun(List<Vertex> bfsRun) {
        this.bfsRun = bfsRun;
    }

    /**
     * Ci < Cj if:
     * (1) |V (Ci)| < |V (Cj)|
     * (2) |V (Ci)| = |V (Cj)| and |E(Ci)| < |E(Cj)|
     * (3) |V (Ci)| = |V (Cj)|, |E(Ci)| = |E(Cj)|, and DFS(Ci) is smaller than DFS(Cj).
     *
     * @param j - other component
     * @return -1 if this is smaller, 1 if this is greater or 0 if equal.
     */
    @Override
    public int compareTo(@NotNull ComponentCode j) {
        int thisVerticesSize = this.graph.getVertices().size();
        int otherVerticesSize = j.graph.getVertices().size();
        if (thisVerticesSize < otherVerticesSize) {
            return 1;
        } else if (thisVerticesSize > otherVerticesSize) {
            return -1;
        }

        // vertices count is equal, checking #2 -
        if (code.size() < j.code.size()) {
            return 1;
        } else if (code.size() > j.code.size()) {
            return -1;
        }

        // code count is equal, checking #3 -
        return DFSCodeUtil.compare(this, j);
    }
}
