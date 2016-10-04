package App.Controller;

import App.Model.Graph;
import App.Model.DegreeContext;
import App.Exceptions.NotRealizedGraphException;
import App.Model.Vertex;
import App.Utils.DemoDataCreator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Keinan.Gilad on 9/23/2016.
 */
public class KDegreeAlgorithm implements IAlgorithm{
    private static Logger logger = Logger.getLogger(KDegreeAlgorithm.class);
    public static final int NOISE_ADDITION = 10;
    private static final Random position = new Random();

    /**
     * Main function
     *
     * @param originalGraph
     * @param k
     * @return anonymized graph
     */
    @Override
    public Graph anonymize(Graph originalGraph, Integer k) {
        DegreeContext[] originalDegrees = getDegreeVector(originalGraph);
        DegreeContext[] annonymizeDegreeVector = degreeAnonymization(originalDegrees, k);
        createAdditionalDegreeVector(originalDegrees, annonymizeDegreeVector);
        Graph annoymizeGraph = null;
        try {
            annoymizeGraph = supergraph(originalGraph, annonymizeDegreeVector);
            return annoymizeGraph;
        } catch (NotRealizedGraphException e) {
            //logger.debug(e.getMessage());

            // add noise to original graph and trying again
            addNoise(originalGraph);
            return anonymize(originalGraph, k);
        }
    }

    private void addNoise(Graph originalGraph) {
        Set<Vertex> vertices = originalGraph.getVertices();
        List<Vertex> vertexList = new ArrayList<>(vertices);

        int edgeAdditions = position.nextInt(NOISE_ADDITION);
        for (int i = 0; i < edgeAdditions; i++) {
            int size = vertices.size();
            int xIdx = position.nextInt(size);
            int yIdx = position.nextInt(size);
            originalGraph.addEdge(vertexList.get(xIdx), vertexList.get(yIdx));
        }
    }

    /**
     * step 0
     *
     * @param originalGraph - original model
     * @return the original vector of degrees sort desc.
     */
    private DegreeContext[] getDegreeVector(Graph originalGraph) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = originalGraph.getVertexToNeighbors();
        Set<Vertex> vertices = vertexToNeighbors.keySet();
        List<DegreeContext> originalDegrees = new ArrayList<>();

        Iterator<Vertex> vertexIterator = vertices.iterator();
        while (vertexIterator.hasNext()) {
            Vertex vertex = vertexIterator.next();
            Set<Vertex> neighbors = vertexToNeighbors.get(vertex);
            DegreeContext vertexDegreeContext = new DegreeContext(vertex, neighbors.size());
            originalDegrees.add(vertexDegreeContext);
        }
        DegreeContext[] result = new DegreeContext[vertices.size()];
        Collections.sort(originalDegrees);
        return originalDegrees.toArray(result);
    }

    /**
     * step 1
     *
     * @param originalDegrees - vector of degrees sorted desc.
     * @param k               - the annonymization level
     * @return the annonymized vector
     */
    private DegreeContext[] degreeAnonymization(DegreeContext[] originalDegrees, Integer k) {
        DegreeContext[] anonymizedDegrees = new DegreeContext[originalDegrees.length];
        for (int i = 0; i < originalDegrees.length; i++) {
            DegreeContext originalDegree = originalDegrees[i];
            anonymizedDegrees[i] = new DegreeContext(originalDegree.getVertex(), originalDegree.getDegree());
        }

        int size = anonymizedDegrees.length;

        //System.out.println("Size: " + size);
        degreeAnonymizationRecursive(0, size, anonymizedDegrees, k);
        return anonymizedDegrees; // they are now anonymized
    }

    private void degreeAnonymizationGroup(int from, int to, DegreeContext[] vector) {
        //System.out.println("Grouping [" + from + ", " + to + "]");
        int degree = vector[from].getDegree();
        for (int idx = from; idx < to; idx++) {
            vector[idx].setDegree(degree); // all in the same group as [i]
        }
    }

    private void degreeAnonymizationRecursive(int from, int to, DegreeContext[] vector, Integer k) {
        int size = to - from;
        if (size < 2 * k) {
            degreeAnonymizationGroup(0, size, vector);
        } else {
            int toNew = to - k;
            degreeAnonymizationGroup(toNew, to, vector);
            degreeAnonymizationRecursive(0, toNew, vector, k);
        }
    }

    private void createAdditionalDegreeVector(DegreeContext[] originalDegrees, DegreeContext[] anonymizeDegreeVector) {
        for (int i = 0; i < anonymizeDegreeVector.length; i++) {
            int anonymizedDegree = anonymizeDegreeVector[i].getDegree();
            int originalDegree = originalDegrees[i].getDegree();
            //System.out.println("anonymizedDegree:" + anonymizedDegree + ", originalDegree:" + originalDegree);
            anonymizeDegreeVector[i].setDegree(anonymizedDegree - originalDegree);
        }
    }

    /**
     * step 2
     *
     * @param originalGraph          - the original graph
     * @param additionalDegreeVector - the anonymized vector left to fill the graph
     * @return constructed graph from the anoynmized vector
     */
    private Graph supergraph(Graph originalGraph, DegreeContext[] additionalDegreeVector) throws NotRealizedGraphException {
        // if the sum of additional vector is odd throw illegalGraph
        int sum = sumVector(additionalDegreeVector);
        if (!(sum % 2 == 0)) {
            throw new NotRealizedGraphException("Additional Graph sum is odd");
        }

        return supergraphRecusive(originalGraph, additionalDegreeVector);
    }

    private Graph supergraphRecusive(Graph originalGraph, DegreeContext[] additionalDegreeVector) throws NotRealizedGraphException {
        // check if there exist a degree with minus value in additional vector and throw exception
        checkMinusDegree(additionalDegreeVector);

        // check if sum equals zero than stop
        int sum = sumVector(additionalDegreeVector);
        if (sum == 0) {
            return originalGraph;
        }

        // pick a random DegreeContext with degree > 0
        DegreeContext degreeContext = nextPositiveDegree(additionalDegreeVector);

        // pick vectors (that are not already connected to vertex) from additional vector and connect new Edge.
        pickAndConnectEdges(originalGraph, degreeContext, additionalDegreeVector);

        // update additional vector values
        degreeContext.setDegree(0); // we connected all of them

        // call recursive again
        return supergraphRecusive(originalGraph, additionalDegreeVector);
    }

    /**
     * choosing vertexes as getDegree count and add new edge which is not already exist in vertexNeighbors
     *
     * @param originalGraph
     * @param degreeContext
     * @param additionalDegreeVector
     */
    private void pickAndConnectEdges(Graph originalGraph, DegreeContext degreeContext, DegreeContext[] additionalDegreeVector) throws NotRealizedGraphException {
        int degreeToAdd = degreeContext.getDegree();
        Map<Vertex, Set<Vertex>> vertexToNeighbors = originalGraph.getVertexToNeighbors();
        Set<Vertex> vertexNeighbors = vertexToNeighbors.get(degreeContext.getVertex());

        for (int i = 0; i < degreeToAdd; i++) {
            DegreeContext randomDegreeContext = nextValidVertexToConnect(additionalDegreeVector, degreeContext, vertexNeighbors);
            Vertex randomVertex = randomDegreeContext.getVertex();

            // connecting this vertex
            originalGraph.addEdge(degreeContext.getVertex(), randomVertex);

            // update lists
            vertexNeighbors.add(randomVertex);
            randomDegreeContext.setDegree(randomDegreeContext.getDegree() - 1); // decrease by 1
        }
    }

    private DegreeContext nextValidVertexToConnect(DegreeContext[] additionalDegreeVector, DegreeContext degreeContext, Set<Vertex> vertexNeighbors) throws NotRealizedGraphException {
        for (int i = 0; i < additionalDegreeVector.length; i++) {
            DegreeContext randomVertex = additionalDegreeVector[i];
            if (randomVertex.getDegree() > 0 && !randomVertex.getVertex().equals(degreeContext.getVertex())) {
                // check if it's not exist in the neighbors list
                if (!vertexNeighbors.contains(randomVertex.getVertex())) {
                    // ok to go
                    return randomVertex;
                }
            }
        }
        throw new NotRealizedGraphException("No more edges to connect");
    }

    private DegreeContext nextPositiveDegree(DegreeContext[] additionalDegreeVector) {
        for (int i = 0; i < additionalDegreeVector.length; i++) {
            DegreeContext degreeContext = additionalDegreeVector[i];
            if (degreeContext.getDegree() > 0) {
                return degreeContext;
            }

        }
        return null;
    }

    private int sumVector(DegreeContext[] additionalDegreeVector) {
        int sum = 0;
        for (int i = 0; i < additionalDegreeVector.length; i++) {
            sum += additionalDegreeVector[i].getDegree();
        }
        return sum;
    }

    private void checkMinusDegree(DegreeContext[] additionalDegreeVector) throws NotRealizedGraphException {
        for (int i = 0; i < additionalDegreeVector.length; i++) {
            if (additionalDegreeVector[i].getDegree() < 0) {
                throw new NotRealizedGraphException("Additional Graph contain minus degree");
            }
        }
    }

    public static void main(String[] agrs) {
        BasicConfigurator.configure();

        DemoDataCreator demoDataCreator = new DemoDataCreator();
        Graph originalGraph = demoDataCreator.getDataSetToModel();
        KDegreeAlgorithm kDegreeAlgo = new KDegreeAlgorithm();

        int k = 5;

        DegreeContext[] originalDegreeVector = kDegreeAlgo.getDegreeVector(originalGraph);
        System.out.println(Arrays.toString(originalDegreeVector));

        /*
        DegreeContext[] anonymizeDegreeVector = kDegreeAlgo.degreeAnonymization(originalDegreeVector, k);
        System.out.println(Arrays.toString(anonymizeDegreeVector));

        kDegreeAlgo.createAdditionalDegreeVector(originalDegreeVector, annonymizeDegreeVector);
        System.out.println(Arrays.toString(anonymizeDegreeVector));*/
        Graph anonymize = kDegreeAlgo.anonymize(originalGraph, k);

        DegreeContext[] anonymizedDegreeVector = kDegreeAlgo.getDegreeVector(anonymize);
        System.out.println(Arrays.toString(anonymizedDegreeVector));
    }
}
