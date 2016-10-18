package App.Controller;

import App.Model.*;
import App.Utils.BFS;
import App.Utils.DFSCodeUtil;
import App.Utils.DegreeUtil;
import App.Utils.DemoDataCreator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Keinan.Gilad on 10/1/2016.
 */
public class KNeighborhoodAlgorithm implements IAlgorithm {
    private static Logger logger = Logger.getLogger(KNeighborhoodAlgorithm.class);

    private static double betta = 1;
    private static double gamma = 1.1;
    BFS bfs = new BFS();

    @Override
    public Graph anonymize(Graph originalGraph, Integer k) {
        Graph originalGraphClone = cloneOriginalGraph(originalGraph);
        List<NeighborhoodContext> vertexList = createNeighborhoodContext(originalGraphClone);
        logger.debug("Done createNeighborhoodContext");
        List<NeighborhoodContext> anonymizedVertexList = new ArrayList<>();

        while (vertexList.size() > 0) {
            // removing head
            NeighborhoodContext seedVertex = vertexList.remove(0);
            Vertex vertex = seedVertex.getVertex();

            logger.debug("Start getCandidates for " + vertex);
            // select candidate set according to k and cost.
            List<NeighborhoodContext> candidates = getCandidates(seedVertex, vertexList, k);
            logger.debug("Done getCandidates for " + vertex);

            logger.debug("Start anonymizedNeighbors group " + vertex);
            // anonymized the candidate set with the seed
            anonymizedNeighbors(seedVertex, candidates, vertexList);
            logger.debug("Done anonymizedNeighbors group " + vertex);

            // update vertexList and anonymizedVertexList:
            anonymizedVertexList.addAll(candidates);
            anonymizedVertexList.add(seedVertex);
            vertexList.removeAll(candidates);
        }
        // done anonymizing all vertices and now copy to graph
        copyNeighborhoodContexts(anonymizedVertexList, originalGraphClone);
        return originalGraphClone;
    }

    private List<NeighborhoodContext> getCandidates(NeighborhoodContext seedVertex, List<NeighborhoodContext> vertexList, Integer k) {
        List<NeighborhoodContext> candidates = new ArrayList<>();
        if (vertexList.size() < (2 * k - 1)) {
            // all remaining vertices
            for (NeighborhoodContext nc : vertexList) {
                candidates.add(nc);
            }
        } else {
            // calculate score and take the lowest k-1 vertices
            for (NeighborhoodContext nc : vertexList) {
                // calculate the cost between the seed and the candidate:
                double cost = calculateCost(seedVertex, nc);
                nc.setCost(cost);
                candidates.add(nc);
            }

            // soring by costs desc.
            Collections.sort(candidates, new CostNeighborhoodComparator());
            // taking the top k-1 candidates:
            candidates = candidates.subList(0, k - 1);
        }

        return candidates;
    }

    private void anonymizedNeighbors(NeighborhoodContext seedVertex, List<NeighborhoodContext> candidates, List<NeighborhoodContext> vertexList) {
        NeighborhoodContext anonymizedNeighborhood = anonymizedNeighborsInner(candidates.get(0), seedVertex, new ArrayList<NeighborhoodContext>(), vertexList);

        for (int i = 1; i < candidates.size(); i++) {
            anonymizedNeighborhood = anonymizedNeighborsInner(candidates.get(i), anonymizedNeighborhood, candidates.subList(0, i), vertexList);
        }
    }

    /**
     * When having changes to candidateJ we apply to all candidates until applyIdx.
     *
     * @param candidateI
     * @param candidateJ
     * @param relevantCandidates
     * @return anonymized neighborhood
     */
    private NeighborhoodContext anonymizedNeighborsInner(NeighborhoodContext candidateI, NeighborhoodContext candidateJ, List<NeighborhoodContext> relevantCandidates, List<NeighborhoodContext> vertexList) {
        NeighborhoodContext anonymizedContext = new NeighborhoodContext();
        List<ComponentCode> anonymizedCode = new ArrayList<>();

        List<ComponentCode> candidateICode = candidateI.getNeighborhoodComponentCode();
        List<ComponentCode> candidateJCode = candidateJ.getNeighborhoodComponentCode();
        int candidateISize = candidateICode.size();
        int candidateJSize = candidateJCode.size();
        Integer[] candidateIPair = new Integer[candidateISize];
        Integer[] candidateJPair = new Integer[candidateJSize];

        // 1. find perfectly pairs
        for (int i = 0; i < candidateISize; i++) {
            ComponentCode componentCodeI = candidateICode.get(i);
            for (int j = 0; j < candidateJSize; j++) {
                ComponentCode componentCodeJ = candidateJCode.get(j);
                if (isMatchCodes(componentCodeI, componentCodeJ)) {
                    // found matches
                    candidateIPair[i] = j;
                    candidateJPair[j] = i;
                }
            }
        }

        // 2. pair components
        int minimalSize = Math.min(candidateISize, candidateJSize);
        int i = 0;
        int j = 0;
        for (int idx = 0; idx < minimalSize; idx++) {
            logger.debug("anonymizedNeighborsInner loop:" + idx);
            // get next I candidate
            i = getNextNotPairCandidateCode(candidateIPair, i);
            ComponentCode componentCodeI = candidateICode.get(i);

            // get next J candidate
            j = getNextNotPairCandidateCode(candidateJPair, j);
            ComponentCode componentCodeJ = candidateJCode.get(j);

            // pair both
            logger.debug("Before pairComponents: I=" + i + " J=" + j);
            anonymizedCode.add(pairComponents(componentCodeI, componentCodeJ, relevantCandidates, j, vertexList));
            candidateIPair[i] = j;
            candidateJPair[j] = i;
        }

        // 3. pairing all leftovers without a match
        for (int idx = 0; idx < candidateISize; idx++) {
            if (candidateIPair[idx] == null) {
                ComponentCode componentCodeI = candidateICode.get(idx);
                anonymizedCode.add(pairComponents(componentCodeI, new ComponentCode(), relevantCandidates, -1, vertexList));
            }
        }

        for (int idx = 0; idx < candidateJSize; idx++) {
            if (candidateJPair[idx] == null) {
                ComponentCode componentCodeJ = candidateJCode.get(idx);
                anonymizedCode.add(pairComponents(new ComponentCode(), componentCodeJ, relevantCandidates, idx, vertexList));
            }
        }

        anonymizedContext.setNeighborhoodComponentCode(anonymizedCode);
        return anonymizedContext;
    }

    private int getNextNotPairCandidateCode(Integer[] candidateIPair, int startIdx) {
        for (int idx = startIdx; idx < candidateIPair.length; idx++) {
            if (candidateIPair[idx] == null) {
                return idx;
            }
        }

        return -1;
    }

    private ComponentCode pairComponents(ComponentCode codeI, ComponentCode codeJ, List<NeighborhoodContext> relevantCandidates, int componentIdxJ, List<NeighborhoodContext> vertexList) {
        logger.debug("Start pairComponents");
        Graph componentGraphI = codeI.getGraph();
        Graph componentGraphJ = codeJ.getGraph();

        // sorting by degree
        List<DegreeContext> degreesI = sortByDegree(componentGraphI);
        List<DegreeContext> degreesJ = sortByDegree(componentGraphJ);

        Vertex vi = null;
        Vertex vj = null;

        if (degreesI.size() > 0 && degreesJ.size() > 0) {
            // 1. finding the highest degree pair
            boolean isFound = false;
            int i, j = 0;

            for (i = 0; i < degreesI.size() && !isFound; i++) {
                DegreeContext degreeContextI = degreesI.get(i);
                if (isFound) {
                    break;
                }
                for (j = 0; j < degreesJ.size() && !isFound; j++) {
                    DegreeContext degreeContextJ = degreesJ.get(j);

                    if (degreeContextI.getDegree() == degreeContextJ.getDegree()) {
                        // found match
                        isFound = true;
                        break;
                    }
                }
            }
            vi = degreesI.get(i).getVertex();
            vj = degreesJ.get(j).getVertex();
        }

        // BFS until all vertices are paired
        int bfsISize = 0;
        int bfsJSize = 0;
        List<Vertex> bfsI = new ArrayList<>();
        List<Vertex> bfsJ = new ArrayList<>();

        if (vi != null) {
            Map<Vertex, Set<Vertex>> vertexToNeighborsI = codeI.getGraph().getVertexToNeighbors();
            bfsI = bfs.bfs(vertexToNeighborsI, vi);
            bfsISize = bfsI.size();
        }
        if (vj != null) {
            Map<Vertex, Set<Vertex>> vertexToNeighborsJ = codeJ.getGraph().getVertexToNeighbors();
            bfsJ = bfs.bfs(vertexToNeighborsJ, vj);
            bfsJSize = bfsJ.size();
        }

        // starting the loop for pairing:
        int max = Math.max(bfsISize, bfsJSize);
        logger.debug("Start pairComponents vertices loop total=" + max);
        for (int idx = 0; idx < max; idx++) {
            logger.debug("pairComponents vertices loop:" + idx);
            Vertex vertexI = bfsI.size() > idx ? bfsI.get(idx) : null;
            Vertex vertexJ = bfsJ.size() > idx ? bfsJ.get(idx) : null;

            // if no such vertex, we add new vertex to the component
            if (vertexI == null) {
                // connect new vertex to the old idx
                NeighborhoodContext vertexToJoin = findUnanonymizedVertexWithLeastDegree(codeI.getGraph().getVertices(), codeJ.getGraph().getVertices(), vertexList);
                if (vertexToJoin != null && bfsI.size() > idx - 1) {
                    vertexI = vertexToJoin.getVertex();
                    addVertexToComponent(codeI, vertexI, bfsI.get(idx - 1));
                } else {
                    continue;
                }

            } else if (vertexJ == null) {
                // connect new vertex to the old idx
                NeighborhoodContext vertexToJoin = findUnanonymizedVertexWithLeastDegree(codeI.getGraph().getVertices(), codeJ.getGraph().getVertices(), vertexList);
                if (vertexToJoin != null && bfsJ.size() > idx - 1) {
                    vertexJ = vertexToJoin.getVertex();
                    addVertexToComponent(codeJ, vertexJ, bfsJ.get(idx - 1));
                } else {
                    continue;
                }

                addVertexToRelevantComponents(relevantCandidates, componentIdxJ, idx, idx - 1, codeI.getGraph().getVertices(), codeJ.getGraph().getVertices(), vertexList);
            }
            Map<Vertex, Set<Vertex>> vertexToNeighborsI = codeI.getGraph().getVertexToNeighbors();
            int vertexIDegree = vertexToNeighborsI.get(vertexI).size();

            Map<Vertex, Set<Vertex>> vertexToNeighborsJ = codeJ.getGraph().getVertexToNeighbors();
            int vertexJDegree = vertexToNeighborsJ.get(vertexJ).size();

            // continue to pair the vertices:
            if (vertexIDegree == vertexJDegree) {
                // match
                continue;
            } else {
                // not matched need to be paired.
                if (vertexIDegree > vertexJDegree) {
                    // need to add [IDegree - JDegree] vertices to componentJ/VertexJ
                    for (int m = vertexJDegree; m < vertexIDegree; m++) {

                        NeighborhoodContext vertexToJoin = findUnanonymizedVertexWithLeastDegree(codeI.getGraph().getVertices(), codeJ.getGraph().getVertices(), vertexList);
                        if (vertexToJoin != null) {
                            codeJ.getGraph().addEdge(vertexJ, vertexToJoin.getVertex());
                        }

                        // fixing all other anonymized components in the same group
                        addVertexToRelevantComponents(relevantCandidates, componentIdxJ, idx, -1, codeI.getGraph().getVertices(), codeJ.getGraph().getVertices(), vertexList);
                    }
                } else {
                    // need to add [JDegree - IDegree] vertices to componentI/VertexI
                    for (int m = vertexIDegree; m < vertexJDegree; m++) {
                        NeighborhoodContext vertexToJoin = findUnanonymizedVertexWithLeastDegree(codeI.getGraph().getVertices(), codeJ.getGraph().getVertices(), vertexList);
                        if (vertexToJoin != null) {
                            // need to fix only componentI.
                            codeI.getGraph().addEdge(vertexI, vertexToJoin.getVertex());
                        }
                    }
                }
            }
        }

        return codeJ;
    }

    private void addVertexToRelevantComponents(List<NeighborhoodContext> relevantCandidates, int componentIdxJ, int innerVertexToJoinBFSIdx, int vertexToJoin2Idx, Set<Vertex> verticesI, Set<Vertex> verticesJ, List<NeighborhoodContext> vertexList) {
        // need also to fix other relevant components, that were anonymized in the same group.
        for (NeighborhoodContext nc : relevantCandidates) {
            ComponentCode componentCode = nc.getNeighborhoodComponentCode().get(componentIdxJ);

            if (vertexToJoin2Idx < 0) {
                NeighborhoodContext outerVertexToJoin = findUnanonymizedVertexWithLeastDegree(verticesI, verticesJ, vertexList);
                if (outerVertexToJoin != null) {
                    addVertexToComponent(componentCode, innerVertexToJoinBFSIdx, outerVertexToJoin.getVertex());
                }
            } else {
                addVertexToComponent(componentCode, innerVertexToJoinBFSIdx, vertexToJoin2Idx);
            }
        }
    }

    private void addVertexToComponent(ComponentCode componentCode, int innerVertexToJoinBFSIdx, int innerVertexToJoinBFSIdx2) {
        List<Vertex> bfsRun = getBFSRun(componentCode);
        addVertexToComponent(componentCode, bfsRun.get(innerVertexToJoinBFSIdx), bfsRun.get(innerVertexToJoinBFSIdx2));
    }

    private void addVertexToComponent(ComponentCode componentCode, int innerVertexToJoinBFSIdx, Vertex outerVertexToJoin) {
        Vertex vi = getVertexByBFSRunIndex(componentCode, innerVertexToJoinBFSIdx);
        addVertexToComponent(componentCode, vi, outerVertexToJoin);
    }

    private Vertex getVertexByBFSRunIndex(ComponentCode componentCode, int vertexToJoinBFSIdx) {
        List<Vertex> bfsRun = getBFSRun(componentCode);
        return bfsRun.get(vertexToJoinBFSIdx);
    }

    private List<Vertex> getBFSRun(ComponentCode componentCode) {
        List<DegreeContext> componentCodeDegrees = sortByDegree(componentCode.getGraph());
        List<Vertex> bfsRun = bfs.bfs(componentCode.getGraph().getVertexToNeighbors(), componentCodeDegrees.get(0).getVertex());
        return bfsRun;
    }

    private void addVertexToComponent(ComponentCode code, Vertex vi, Vertex vj) {
        code.getGraph().addEdge(vi, vj);
    }

    private NeighborhoodContext findUnanonymizedVertexWithLeastDegree(Set<Vertex> verticesI, Set<Vertex> verticesJ, List<NeighborhoodContext> vertexList) {
        for (int i = vertexList.size() - 1; i >= 0; i--) {
            NeighborhoodContext neighborhoodContext = vertexList.get(i);
            if (!neighborhoodContext.isAnonymized() && !verticesI.contains(neighborhoodContext.getVertex()) && !verticesJ.contains(neighborhoodContext.getVertex())) {
                return neighborhoodContext;
            }
        }
        // TODO - return anonymized vertex (set to unanonymized and add to VertexList).
        return null;
    }

    private List<DegreeContext> sortByDegree(Graph componentGraphJ) {
        return DegreeUtil.sortByDegree(componentGraphJ);
    }

    private boolean isMatchCodes(ComponentCode codeI, ComponentCode codeJ) {
        List<Edge> codeICode = codeI.getCode();
        List<Edge> codeJCode = codeJ.getCode();
        if (codeICode == null || codeJCode == null || codeICode.size() == 0 || codeJCode.size() == 0) {
            return false;
        }

        if (codeICode.size() != codeJCode.size()) {
            return false;
        }

        for (int i = 0; i < codeICode.size(); i++) {
            Edge edgeI = codeICode.get(i);
            Edge edgeJ = codeJCode.get(i);
            if (!edgeI.equals(edgeJ)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param seedVertex - seed vertex neighborhood
     * @param nc         - other vertex's neighborhood
     * @return calculating how many edges were added and how many vertices were added to the assume isomorphic neighbors with both vertices.
     */
    private double calculateCost(NeighborhoodContext seedVertex, NeighborhoodContext nc) {
        List<ComponentCode> seedCode = seedVertex.getNeighborhoodComponentCode();
        List<ComponentCode> otherCode = nc.getNeighborhoodComponentCode();
        int edgesAdded = 0;
        int verticesAdded = 0;

        int seedSize = seedCode.size();
        int otherSize = otherCode.size();
        int size = Math.max(seedSize, otherSize);

        for (int i = 0; i < size; i++) {
            Set<Edge> seedComponentEdges = null;
            Set<Edge> otherComponentEdges = null;

            if (i < seedSize) {
                seedComponentEdges = seedCode.get(i).getGraph().getEdges();
            }
            if (i < otherSize) {
                otherComponentEdges = otherCode.get(i).getGraph().getEdges();
            }

            // calculating added changes:
            if (seedComponentEdges != null && otherComponentEdges != null) {
                for (Edge ei : seedComponentEdges) {
                    if (!otherComponentEdges.contains(ei)) {
                        edgesAdded += 1;
                    }
                }
                for (Edge ei : otherComponentEdges) {
                    if (!seedComponentEdges.contains(ei)) {
                        edgesAdded += 1;
                    }
                }
            } else if (seedComponentEdges != null && otherComponentEdges == null) {
                edgesAdded += seedComponentEdges.size();
            } else if (seedComponentEdges == null && otherComponentEdges != null) {
                edgesAdded += otherComponentEdges.size();
            } else {
                // 0.
            }

            // checking vertices:
            Set<Vertex> seedVerticesInComponent = new HashSet<>();
            Set<Vertex> otherVerticesInComponent = new HashSet<>();
            if (i < seedSize) {
                seedVerticesInComponent = seedCode.get(i).getGraph().getVertices();
            }
            if (i < otherSize) {
                otherVerticesInComponent = otherCode.get(i).getGraph().getVertices();
            }
            verticesAdded += Math.abs(seedVerticesInComponent.size() - otherVerticesInComponent.size());
        }

        return edgesAdded * betta + verticesAdded * gamma;
    }

    /**
     * updating the edges and the vertexToNeighbors map
     *
     * @param anonymizedVertexList
     * @param originalGraphClone
     */
    private void copyNeighborhoodContexts(List<NeighborhoodContext> anonymizedVertexList, Graph originalGraphClone) {
        Set<Edge> allEdges = new HashSet<>();
        Map<Vertex, Set<Vertex>> vertexToNeighbors = new HashMap<>();

        for (NeighborhoodContext nc : anonymizedVertexList) {
            List<ComponentCode> ncc = nc.getNeighborhoodComponentCode();
            for (ComponentCode code : ncc) {
                Graph graph = code.getGraph();
                allEdges.addAll(graph.getEdges());
                vertexToNeighbors.putAll(graph.getVertexToNeighbors());
            }
        }

        // the original vertices did not change.
        // updating vertex to neighbors and edges:
        originalGraphClone.setVertexToNeighbors(vertexToNeighbors);
        originalGraphClone.setEdges(allEdges);
    }

    private List<NeighborhoodContext> createNeighborhoodContext(Graph anonymized) {
        List<NeighborhoodContext> neighborhoodList = new ArrayList<>();
        Map<Vertex, Set<Vertex>> vertexToNeighbors = anonymized.getVertexToNeighbors();
        for (Vertex v : anonymized.getVertices()) {
            logger.debug("createNeighborhoodContext for:" + v.toString());
            NeighborhoodContext context = new NeighborhoodContext();
            context.setAnonymized(false);
            context.setVertex(v);
            Set<Vertex> neighborhoodVertices = vertexToNeighbors.get(v);
            context.setVertices(neighborhoodVertices.size());
            List<Edge> neighborhoodEdges = getNeighborhoodEdges(v, neighborhoodVertices, anonymized.getEdges());
            context.setEdges(neighborhoodEdges.size());
            neighborhoodList.add(context);

            // Calculate the code for each neighborhood and component:
            List<ComponentCode> componentCodes = calculateNCC(neighborhoodVertices, neighborhoodEdges, v);
            logger.debug("found " + componentCodes.size() + " components");
            context.setNeighborhoodComponentCode(componentCodes);
        }

        Collections.sort(neighborhoodList);
        return neighborhoodList;
    }


    /**
     * @param neighborsVertices - all of the neighborhood's vertices
     * @param neighborsEdges    - all of the neighborhood's edges
     * @param v                 - the vertex in the middle of the neighborhood
     * @return list of components with their NCC
     */
    private List<ComponentCode> calculateNCC(Set<Vertex> neighborsVertices, List<Edge> neighborsEdges, Vertex v) {
        List<ComponentCode> components = new ArrayList<>();
        Map<Vertex, ComponentCode> vertexToComponent = new HashMap<>();

        // first, separate to components
        // iterating all edges in neighborhood
        for (Edge e : neighborsEdges) {
            Vertex v0 = e.getV0();
            Vertex v1 = e.getV1();

            // connection between two vertices not with v.
            if (!v0.equals(v) && !v1.equals(v)) {
                // both are in the same component.
                // if exist
                ComponentCode v0ComponentCode = vertexToComponent.get(v0);
                ComponentCode v1ComponentCode = vertexToComponent.get(v1);

                if (v0ComponentCode != null) {
                    v0ComponentCode.getGraph().addEdge(v0, v1);
                    vertexToComponent.put(v1, v0ComponentCode);
                } else if (v1ComponentCode != null) {
                    v1ComponentCode.getGraph().addEdge(v0, v1);
                    vertexToComponent.put(v0, v1ComponentCode);
                } else {
                    // new- creating new component
                    ComponentCode component = new ComponentCode();
                    component.getGraph().addEdge(v0, v1);
                    vertexToComponent.put(v0, component);
                    vertexToComponent.put(v1, component);
                    components.add(component);
                }
            }
        }

        // iterating all separated vertices which don't have any edge (except to v)
        Iterator<Vertex> iterator = neighborsVertices.iterator();
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            if (!v.equals(vertex) && !vertexToComponent.containsKey(vertex)) {
                // new- creating new component
                ComponentCode component = new ComponentCode();
                component.getGraph().getVertices().add(vertex);
                components.add(component);
            }
        }

        // then, calculate minDFS-Code for each one.
        for (ComponentCode componentCode : components) {
            DFSCodeUtil.calculateMinimumCode(componentCode);
        }

        // sorting
        Collections.sort(components);
        logger.debug("Biggest component size is:" + components.get(0).getGraph().getVertices().size());
        // return the NCC
        return components;
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
        Graph originalGraph = demoDataCreator.generateGraph();
        KNeighborhoodAlgorithm kDegreeAlgo = new KNeighborhoodAlgorithm();
        int k = 5;

        Graph anonymize = kDegreeAlgo.anonymize(originalGraph, k);
        System.out.println(anonymize.getVertices());
    }
}
