package Model;

import java.util.List;

/**
 * Created by Keinan.Gilad on 9/14/2016.
 */
public class ResultContext {

    private List<Vertice> beforeVertices;
    private List<Vertice> afterVertices;
    private List<Edge> beforeEdges;
    private List<Edge> afterEdges;

    private String durationLabelValue;
    private String beforeVerticesLabelValue;
    private String beforeEdgesLabelValue;
    private String afterVerticesLabelValue;
    private String afterVerticesAddedLabelValue;
    private String afterVerticesRemovedLabelValue;
    private String afterEdgesLabelValue;
    private String afterEdgesAddedLabelValue;
    private String afterEdgesRemovedLabelValue;
    private String afterObfuscationLeveldLabelValue;

    public List<Vertice> getBeforeVertices() {
        return beforeVertices;
    }

    public void setBeforeVertices(List<Vertice> beforeVertices) {
        this.beforeVertices = beforeVertices;
    }

    public List<Vertice> getAfterVertices() {
        return afterVertices;
    }

    public void setAfterVertices(List<Vertice> afterVertices) {
        this.afterVertices = afterVertices;
    }

    public List<Edge> getBeforeEdges() {
        return beforeEdges;
    }

    public void setBeforeEdges(List<Edge> beforeEdges) {
        this.beforeEdges = beforeEdges;
    }

    public List<Edge> getAfterEdges() {
        return afterEdges;
    }

    public void setAfterEdges(List<Edge> afterEdges) {
        this.afterEdges = afterEdges;
    }

    public String getDurationLabelValue() {
        return durationLabelValue;
    }

    public void setDurationLabelValue(String durationLabelValue) {
        this.durationLabelValue = durationLabelValue;
    }

    public String getBeforeVerticesLabelValue() {
        return beforeVerticesLabelValue;
    }

    public void setBeforeVerticesLabelValue(String beforeVerticesLabelValue) {
        this.beforeVerticesLabelValue = beforeVerticesLabelValue;
    }

    public String getBeforeEdgesLabelValue() {
        return beforeEdgesLabelValue;
    }

    public void setBeforeEdgesLabelValue(String beforeEdgesLabelValue) {
        this.beforeEdgesLabelValue = beforeEdgesLabelValue;
    }

    public String getAfterVerticesLabelValue() {
        return afterVerticesLabelValue;
    }

    public void setAfterVerticesLabelValue(String afterVerticesLabelValue) {
        this.afterVerticesLabelValue = afterVerticesLabelValue;
    }

    public String getAfterVerticesAddedLabelValue() {
        return afterVerticesAddedLabelValue;
    }

    public void setAfterVerticesAddedLabelValue(String afterVerticesAddedLabelValue) {
        this.afterVerticesAddedLabelValue = afterVerticesAddedLabelValue;
    }

    public String getAfterVerticesRemovedLabelValue() {
        return afterVerticesRemovedLabelValue;
    }

    public void setAfterVerticesRemovedLabelValue(String afterVerticesRemovedLabelValue) {
        this.afterVerticesRemovedLabelValue = afterVerticesRemovedLabelValue;
    }

    public String getAfterEdgesLabelValue() {
        return afterEdgesLabelValue;
    }

    public void setAfterEdgesLabelValue(String afterEdgesLabelValue) {
        this.afterEdgesLabelValue = afterEdgesLabelValue;
    }

    public String getAfterEdgesAddedLabelValue() {
        return afterEdgesAddedLabelValue;
    }

    public void setAfterEdgesAddedLabelValue(String afterEdgesAddedLabelValue) {
        this.afterEdgesAddedLabelValue = afterEdgesAddedLabelValue;
    }

    public String getAfterEdgesRemovedLabelValue() {
        return afterEdgesRemovedLabelValue;
    }

    public void setAfterEdgesRemovedLabelValue(String afterEdgesRemovedLabelValue) {
        this.afterEdgesRemovedLabelValue = afterEdgesRemovedLabelValue;
    }

    public String getAfterObfuscationLeveldLabelValue() {
        return afterObfuscationLeveldLabelValue;
    }

    public void setAfterObfuscationLeveldLabelValue(String afterObfuscationLeveldLabelValue) {
        this.afterObfuscationLeveldLabelValue = afterObfuscationLeveldLabelValue;
    }
}
