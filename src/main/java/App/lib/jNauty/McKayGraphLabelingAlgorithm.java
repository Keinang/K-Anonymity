package App.lib.jNauty;

import App.Common.Utils.DegreeUtil;
import App.Model.Edge;
import App.Model.Graph;
import App.Model.Vertex;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Implementation of McKay's canonical graph labeling algorithm
 *
 * @param <V>
 * @param <E>
 */
public class McKayGraphLabelingAlgorithm<V extends Vertex, E extends Edge> {
    private static Logger logger = Logger.getLogger(McKayGraphLabelingAlgorithm.class);

    public List<Permutation> findAutomorphisms(Graph graph) {
        // get information from graph:
        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();
        Set<Edge> edges = graph.getEdges();

        // create ordered partition
        OrderedPartition<Vertex> pi = getVertexOrderedPartition(graph.getVertices());
        // refinement
        OrderedPartition<Vertex> refined = refinementProcedure(pi, vertexToNeighbors);
        // craeting search tree
        SearchTree<Vertex> tree = createSearchTree(refined, vertexToNeighbors);
        List<SearchTreeNode<Vertex>> terminalNodes = tree.getTerminalNodes();
        // finding automorphisms
        return findAutomorphismsInner(terminalNodes, graph.getVertices(), vertexToNeighbors, edges);
    }

    private OrderedPartition<Vertex> getVertexOrderedPartition(List<Vertex> vertices) {
        List<List<Vertex>> allVertices = new ArrayList<List<Vertex>>();
        allVertices.add(vertices);
        return new OrderedPartition<Vertex>(allVertices);
    }

    private OrderedPartition<Vertex> refinementProcedure(OrderedPartition<Vertex> pi, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        OrderedPartition<Vertex> tau = new OrderedPartition<Vertex>(pi.getPartition());
        List<Pair<List<Vertex>, List<Vertex>>> B = new ArrayList<Pair<List<Vertex>, List<Vertex>>>();
        Map<Integer, List<Vertex>> degreesMap = new HashMap<Integer, List<Vertex>>();

        while (true) {
            B.clear();
            for (List<Vertex> Vi : tau.getPartition()) {
                for (List<Vertex> Vj : tau.getPartition()) {
                    //check if Vj shatters Vi
                    int deg = deg(Vi.get(0), Vj, vertexToNeighbors);
                    for (int i = 1; i < Vi.size(); i++) {
                        if (deg(Vi.get(i), Vj, vertexToNeighbors) != deg) {
                            B.add(new Pair<List<Vertex>, List<Vertex>>(Vi, Vj));
                            break;
                        }
                    }
                }
            }

            if (B.size() == 0)
                break;

            //System.out.println("B: " + B);

            //now find the minimum element
            Pair<List<Vertex>, List<Vertex>> minimalPair = findMinimal(B, vertexToNeighbors);

            //System.out.println("Minimal pair is " + minimalPair );

            //now replace Vi with X1,X2,...Xt
            degreesMap.clear();
            List<Vertex> Vi = minimalPair.getKey();
            List<Vertex> Vj = minimalPair.getValue();

            List<Integer> degrees = new ArrayList<Integer>();
            for (Vertex v : Vi) {
                Integer deg = deg(v, Vj, vertexToNeighbors);
                List<Vertex> verticesWithDegree = degreesMap.get(deg);
                if (verticesWithDegree == null) {
                    verticesWithDegree = new ArrayList<Vertex>();
                    degreesMap.put(deg, verticesWithDegree);
                }
                verticesWithDegree.add(v);
                if (!degrees.contains(deg))
                    degrees.add(deg);
            }

            //sort, to insert those with lower degrees first
            Collections.sort(degrees);

            List<List<Vertex>> replacements = new ArrayList<List<Vertex>>();
            for (Integer degree : degrees) {
                replacements.add(degreesMap.get(degree));
            }

            tau.replace(Vi, replacements);

        }
        return tau;
    }

    /*
     * For lexicographic total order
     * (a,b) <= (c,d) if a<c or a=c and b<=d
     */
    private Pair<List<Vertex>, List<Vertex>> findMinimal(List<Pair<List<Vertex>, List<Vertex>>> B, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        Pair<List<Vertex>, List<Vertex>> minimalPair = null;
        String minimalBinary1 = null;
        String minimalBinary2 = null;

        for (Pair<List<Vertex>, List<Vertex>> separationPair : B) {

            String binary1 = BinaryRepresentation.binaryRepresenatation(separationPair.getKey(), vertexToNeighbors);

            if (minimalPair == null) {
                minimalPair = separationPair;
                minimalBinary1 = binary1;
            } else {

                //compare current to minimal

                if (binary1.compareTo(minimalBinary1) < 0) {
                    minimalPair = separationPair;
                    minimalBinary1 = binary1;
                    minimalBinary2 = null;
                } else if (binary1.compareTo(minimalBinary1) == 0) {
                    if (minimalBinary2 == null)
                        minimalBinary2 = BinaryRepresentation.binaryRepresenatation(minimalPair.getValue(), vertexToNeighbors);

                    String binary2 = BinaryRepresentation.binaryRepresenatation(separationPair.getValue(), vertexToNeighbors);
                    if (binary2.compareTo(minimalBinary2) <= 0) {
                        minimalPair = separationPair;
                        minimalBinary1 = binary1;
                        minimalBinary2 = binary2;
                    }

                }
            }
        }

        //System.out.println("Minimap pair is: " + minimalPair.getKey() + " " + minimalPair.getValue());
        return minimalPair;
    }

    private SearchTree<Vertex> createSearchTree(OrderedPartition<Vertex> rootPartition, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        //System.out.println("Root " + rootPartition);
        SearchTree<Vertex> tree = new SearchTree<Vertex>(rootPartition);
        SearchTreeNode<Vertex> root = tree.getRoot();
        createSearchTree(root, vertexToNeighbors);
        return tree;
    }

    private void createSearchTree(SearchTreeNode<Vertex> currentNode, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        //split tree note, create children, process children
        OrderedPartition<Vertex> currentPartition = currentNode.getNodePartition();
        List<Vertex> firstNontrivialrPart = currentPartition.getFirstNontrivialPart();
        if (firstNontrivialrPart == null)
            return;
        //System.out.println("Current partition: " + currentPartition);
        for (Vertex u : firstNontrivialrPart) {
            //System.out.println("Splitting by " + u);
            OrderedPartition<Vertex> partition = splitPartition(u, currentPartition);
            partition = refinementProcedure(partition, vertexToNeighbors);
            //System.out.println(partition);
            new SearchTreeNode<Vertex>(partition, u, currentNode);
        }
        for (SearchTreeNode<Vertex> node : currentNode.getChildren()) {
            createSearchTree(node, vertexToNeighbors);
        }
    }

    private OrderedPartition<Vertex> splitPartition(Vertex u, OrderedPartition<Vertex> pi) {

        //find part which contains u
        List<Vertex> Vi = pi.partContainingVertex(u);
        OrderedPartition<Vertex> piPrim = new OrderedPartition<Vertex>();
        int i = pi.getPartition().indexOf(Vi);
        for (int j = 0; j < pi.getPartition().size(); j++) {
            List<Vertex> currentPart = pi.getPartition().get(j);
            if (j != i) {
                piPrim.addPart(currentPart);
                continue;
            }
            //add trivial partition {u}
            //add Vi/u
            List<Vertex> uPart = new ArrayList<Vertex>();
            uPart.add(u);
            piPrim.addPart(uPart);
            List<Vertex> ViPart = new ArrayList<Vertex>();
            ViPart.addAll(Vi);
            ViPart.remove(u);
            piPrim.addPart(ViPart);
        }

        return piPrim;
    }

    private List<Permutation> findAutomorphismsInner(List<SearchTreeNode<Vertex>> terminalNodes, List<Vertex> vertices, Map<Vertex, Set<Vertex>> vertexToNeighbors, Set<Edge> edges) {
        logger.debug("Start findAutomorphismsInner");
        List<Permutation> ret = new ArrayList<Permutation>();

        //calculate permutations
        List<Permutation> allPermutations = new ArrayList<Permutation>();
        for (SearchTreeNode<Vertex> node : terminalNodes) {
            Permutation p = permutation(node.getNodePartition(), vertices);
            allPermutations.add(p);
        }

        for (int i = 0; i < allPermutations.size(); i++) {
            for (int j = i; j < allPermutations.size(); j++) {
                Permutation p1 = allPermutations.get(i);
                Permutation p2 = allPermutations.get(j);

                //calculate p1 * (p2)^-1
                Permutation inverse = p2.inverse();
                Permutation p = p1.mul(inverse);

                if (!ret.contains(p)) {
                    if (checkAutomorphism(p, vertices, vertexToNeighbors, edges)) {
                        ret.add(p);
                    }
                }
            }
        }
        logger.debug("Done findAutomorphismsInner");
        return ret;
    }

    private boolean checkAutomorphism(Permutation permutation, List<Vertex> vertices, Map<Vertex, Set<Vertex>> vertexToNeighbors, Set<Edge> edges) {
        for (Edge e : edges) {
            Vertex v0 = e.getV0();
            Vertex v1 = e.getV1();

            Integer v0Index = vertices.indexOf(v0);
            Integer v1Index = vertices.indexOf(v1);

            Integer mappedV0Index = permutation.getPermutation().get(v0Index);
            Integer mappedV1Index = permutation.getPermutation().get(v1Index);

            Vertex v0Mapped = vertices.get(mappedV0Index);
            Vertex v1Mapped = vertices.get(mappedV1Index);

            if (!DegreeUtil.isEdgeBetween(v0Mapped, v1Mapped, vertexToNeighbors)) {
                return false;
            }
        }
        return true;
    }

    private Permutation permutation(OrderedPartition<Vertex> discretePartition, List<Vertex> vertices) {
        Map<Integer, Integer> permutation = new HashMap<Integer, Integer>();
        for (int i = 0; i < discretePartition.getPartition().size(); i++) {
            List<Vertex> part = discretePartition.getPartition().get(i);
            Vertex v = part.get(0); //the only one
            Integer vertexIndex = vertices.indexOf(v);
            permutation.put(vertexIndex, i);
        }
        return new Permutation(permutation);
    }


    private int deg(Vertex v, List<Vertex> Vj, Map<Vertex, Set<Vertex>> vertexToNeighborsm) {
        int deg = 0;
        Set<Vertex> vertices = vertexToNeighborsm.get(v);
        for (Vertex test : vertices) {
            if (Vj.contains(test)) {
                deg++;
            }
        }
        return deg;
    }
}
