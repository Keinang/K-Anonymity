package App.Controller;

import App.Model.Edge;
import App.Model.Graph;
import App.Model.NeighborhoodContext;
import App.Model.Vertex;
import App.Utils.DemoDataCreator;
import org.apache.log4j.BasicConfigurator;

import java.util.*;

/**
 * Created by Keinan.Gilad on 10/1/2016.
 */
public class KNeighborhoodAlgorithm implements IAlgorithm {

    private static double betta = 1;
    private static double gamma = 1.1;

    @Override
    public Graph anonymize(Graph originalGraph, Integer k) {
        Graph originalGraphClone = cloneOriginalGraph(originalGraph);
        List<NeighborhoodContext> vertexList = createNeighborhoodContext(originalGraphClone);
        List<NeighborhoodContext> anonymizedVertexList = new ArrayList<>();

        while (vertexList.size() > 0) {
            // removing head
            NeighborhoodContext seedVertex = vertexList.remove(0);

            // select candidate set according to k and cost.
            Set<NeighborhoodContext> candidateSet = getCandidates(seedVertex, vertexList, k);

            // anonymized the candidate set with the seed
            anonymizedVertexList.addAll(anonymizeVertices(seedVertex, candidateSet));
        }
        // done anonymizing all vertices and now copy to graph
        copyNeighborhoodContext(anonymizedVertexList, originalGraphClone);
        return originalGraphClone;
    }

    private Collection<? extends NeighborhoodContext> anonymizeVertices(NeighborhoodContext seedVertex, Set<NeighborhoodContext> candidateSet) {
        return null;
    }

    private Set<NeighborhoodContext> getCandidates(NeighborhoodContext seedVertex, List<NeighborhoodContext> vertexList, Integer k) {
        Set<NeighborhoodContext> candidateSet = new HashSet<>();
        if (vertexList.size() < (2 * k - 1)) {
            // all remaining vertices
            for (NeighborhoodContext nc : vertexList) {
                candidateSet.add(nc);
            }
        } else {
            // calculate score and take the lowest k-1.
            for (NeighborhoodContext nc : vertexList) {
                // calculate the cost between the seed and the candidate:
                int cost = calculateCost(seedVertex, nc);

                // prepare the model:
                //nc.setCost(cost);
            }
            // soring the costs desc.
            //Collections.sort(;

            // taking the top k-1 with smallest cost
            /*for (int i = 0; i < k - 1; i++) {
                candidateSet.add(nc));
            }*/
        }

        return candidateSet;
    }

    private int calculateCost(NeighborhoodContext seedVertex, NeighborhoodContext nc) {
        // calculating how many edges were added and how many vertices were added to the assume isomorphic neighbors with both vertices.


        // TODO - covert to NCC, if both NCC are equal than they are isomorphic.
        return 0;
    }

    /**
     * updating the edges and the vertexToNeighbors map
     *
     * @param anonymizedVertexList
     * @param originalGraphClone
     */
    private void copyNeighborhoodContext(List<NeighborhoodContext> anonymizedVertexList, Graph originalGraphClone) {
        Set<Edge> allEdges = new HashSet<>();
        Map<Vertex, Set<Vertex>> vertexToNeighboors = new HashMap<>();
        for (NeighborhoodContext nc : anonymizedVertexList) {
            Vertex vertex = nc.getVertex();
            allEdges.addAll(nc.getNeighborsEdges());
            vertexToNeighboors.put(vertex, nc.getNeighborsVertices());
        }

        originalGraphClone.setVertexToNeighbors(vertexToNeighboors);
        originalGraphClone.setEdges(allEdges);
    }

    private List<NeighborhoodContext> createNeighborhoodContext(Graph anonymized) {
        List<NeighborhoodContext> vertexList = new ArrayList<>();
        Map<Vertex, Set<Vertex>> vertexToNeighbors = anonymized.getVertexToNeighbors();
        for (Vertex v : anonymized.getVertices()) {
            NeighborhoodContext context = new NeighborhoodContext();
            context.setAnonymized(false);
            context.setVertex(v);
            Set<Vertex> neighborhoodVertices = vertexToNeighbors.get(v);
            context.setNeighborsVertices(neighborhoodVertices);
            context.setNeighborsEdges(getNeighborhoodEdges(v, neighborhoodVertices, anonymized.getEdges()));
            vertexList.add(context);
        }

        Collections.sort(vertexList);
        //System.out.println(vertexList);
        return vertexList;
    }

    private List<Edge> getNeighborhoodEdges(Vertex v, Set<Vertex> neighborhoodVertices, Set<Edge> edges) {
        // prepare the whole relevant vertices list
        Set<Vertex> allRelevantVertices = new HashSet<>(neighborhoodVertices);
        allRelevantVertices.add(v);

        // iterating the edges to find relevant edges with relevant vertices.
        List<Edge> neighborhoodEdges = new ArrayList<>();
        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge next = iterator.next();
            Vertex v0 = next.getV0();
            Vertex v1 = next.getV1();

            // adding relevant edge if vertices are relevant
            if (allRelevantVertices.contains(v0) && allRelevantVertices.contains(v1)) {
                neighborhoodEdges.add(next);
            }
        }
        return neighborhoodEdges;
    }

    private Graph cloneOriginalGraph(Graph originalGraph) {
        Graph result = new Graph();
        result.setEdges(originalGraph.getEdges());
        result.setVertices(originalGraph.getVertices());
        result.setDataSet(originalGraph.getDataSet());
        result.setVertexToNeighbors(originalGraph.getVertexToNeighbors());
        return result;
    }

    public static void main(String[] agrs) {
        BasicConfigurator.configure();

        DemoDataCreator demoDataCreator = new DemoDataCreator();
        Graph originalGraph = demoDataCreator.getDataSetToModel();
        KNeighborhoodAlgorithm kDegreeAlgo = new KNeighborhoodAlgorithm();
        int k = 5;

        Graph anonymize = kDegreeAlgo.anonymize(originalGraph, k);
        //System.out.println(anonymize.getVertices());
    }
}
