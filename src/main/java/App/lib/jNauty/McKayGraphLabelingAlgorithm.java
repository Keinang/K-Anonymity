package App.lib.jNauty;

import App.Model.Edge;
import App.Model.Graph;
import App.Model.Vertex;
import App.Utils.DegreeUtil;
import javafx.util.Pair;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Implementation of McKay's canonical graph labeling algorithm
 *
 * @param <E>
 * @author xx
 */
public class McKayGraphLabelingAlgorithm<V extends Vertex, E extends Edge> {
    private Graph graph;
    private BinaryRepresentation<V, E> binaryRepresenatation;


    public McKayGraphLabelingAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public List<Permutation> findAutomorphisms() {
        List<Vertex> vertices = CollectionUtils.arrayToList(graph.getVertices().toArray());
        List<List<Vertex>> allVertices = new ArrayList<List<Vertex>>();
        allVertices.add(vertices);
        OrderedPartition<Vertex> pi = new OrderedPartition<Vertex>(allVertices);
        binaryRepresenatation = new BinaryRepresentation<V, E>(graph);
        OrderedPartition<Vertex> refined = refinementProcedure(pi);

        SearchTree<Vertex> tree = createSearchTree(refined);
        List<SearchTreeNode<Vertex>> terminalNodes = tree.getTerminalNodes();

        //canonicalIsomorphism(terminalNodes);
        return findAutomorphisms(terminalNodes);
    }

    private OrderedPartition<Vertex> refinementProcedure(OrderedPartition<Vertex> pi) {
        OrderedPartition<Vertex> tau = new OrderedPartition<Vertex>(pi.getPartition());
        List<Pair<List<Vertex>, List<Vertex>>> B = new ArrayList<Pair<List<Vertex>, List<Vertex>>>();
        Map<Integer, List<Vertex>> degreesMap = new HashMap<Integer, List<Vertex>>();

        while (true) {
            B.clear();
            for (List<Vertex> Vi : tau.getPartition()) {
                for (List<Vertex> Vj : tau.getPartition()) {
                    //check if Vj shatters Vi
                    int deg = deg(Vi.get(0), Vj);
                    for (int i = 1; i < Vi.size(); i++) {
                        if (deg(Vi.get(i), Vj) != deg) {
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
            Pair<List<Vertex>, List<Vertex>> minimalPair = findMinimal(B);

            //System.out.println("Minimal pair is " + minimalPair );

            //now replace Vi with X1,X2,...Xt
            degreesMap.clear();
            List<Vertex> Vi = minimalPair.getKey();
            List<Vertex> Vj = minimalPair.getValue();

            List<Integer> degrees = new ArrayList<Integer>();
            for (Vertex v : Vi) {
                Integer deg = deg(v, Vj);
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
    private Pair<List<Vertex>, List<Vertex>> findMinimal(List<Pair<List<Vertex>, List<Vertex>>> B) {
        Pair<List<Vertex>, List<Vertex>> minimalPair = null;
        String minimalBinary1 = null;
        String minimalBinary2 = null;

        for (Pair<List<Vertex>, List<Vertex>> separationPair : B) {

            String binary1 = binaryRepresenatation.binaryRepresenatation(separationPair.getKey());

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
                        minimalBinary2 = binaryRepresenatation.binaryRepresenatation(minimalPair.getValue());

                    String binary2 = binaryRepresenatation.binaryRepresenatation(separationPair.getValue());
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

    private SearchTree<Vertex> createSearchTree(OrderedPartition<Vertex> rootPartition) {
        //System.out.println("Root " + rootPartition);
        SearchTree<Vertex> tree = new SearchTree<Vertex>(rootPartition);
        SearchTreeNode<Vertex> root = tree.getRoot();
        createSearchTree(root);
        return tree;
    }

    private void createSearchTree(SearchTreeNode<Vertex> currentNode) {
        //split tree note, create children, process children
        OrderedPartition<Vertex> currentPartition = currentNode.getNodePartition();
        List<Vertex> firstNontrivialrPart = currentPartition.getFirstNontrivialPart();
        if (firstNontrivialrPart == null)
            return;
        //System.out.println("Current partition: " + currentPartition);
        for (Vertex u : firstNontrivialrPart) {
            //System.out.println("Splitting by " + u);
            OrderedPartition<Vertex> partition = splitPartition(u, currentPartition);
            partition = refinementProcedure(partition);
            //System.out.println(partition);
            new SearchTreeNode<Vertex>(partition, u, currentNode);
        }
        for (SearchTreeNode<Vertex> node : currentNode.getChildren()) {
            createSearchTree(node);
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


    @SuppressWarnings("unused")
    private OrderedPartition<Vertex> canonicalIsomorphism(List<SearchTreeNode<Vertex>> terminalNodes) {
        OrderedPartition<Vertex> maxPartition = null;
        String maxBinary = null;
        for (SearchTreeNode<Vertex> node : terminalNodes) {
            OrderedPartition<Vertex> partition = node.getNodePartition();
            String binary = binaryRepresenatation.binaryRepresenatation(partition.getVerticesInOrder());
            if (maxBinary != null)
                System.out.println(binary.compareTo(maxBinary));

            if (maxBinary == null || binary.compareTo(maxBinary) == 1) {
                maxPartition = partition;
                maxBinary = binary;
            }

        }
        return maxPartition;
    }

    private List<Permutation> findAutomorphisms(List<SearchTreeNode<Vertex>> terminalNodes) {
        List<Permutation> ret = new ArrayList<Permutation>();

        //calculate permutations
        List<Permutation> allPermutations = new ArrayList<Permutation>();
        for (SearchTreeNode<Vertex> node : terminalNodes) {
            Permutation p = permutation(node.getNodePartition());
            allPermutations.add(p);
        }

        for (int i = 0; i < allPermutations.size(); i++)
            for (int j = i; j < allPermutations.size(); j++) {
                Permutation p1 = allPermutations.get(i);
                Permutation p2 = allPermutations.get(j);

                //calculate p1 * (p2)^-1
                Permutation inverse = p2.inverse();
                Permutation p = p1.mul(inverse);

                if (!ret.contains(p)) {
                    if (checkAutomorphism(p)) {
                        ret.add(p);
                    }
                }
            }
        return ret;
    }

    private boolean checkAutomorphism(Permutation permutation) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();
        for (Edge e : graph.getEdges()) {
            Vertex v0 = e.getV0();
            Vertex v1 = e.getV1();

            Integer v0Index = DegreeUtil.indexOf(v0,graph.getVertices());
            Integer v1Index = DegreeUtil.indexOf(v1,graph.getVertices());

            Integer mappedV0Index = permutation.getPermutation().get(v0Index);
            Integer mappedV1Index = permutation.getPermutation().get(v1Index);

            Vertex v0Mapped = DegreeUtil.getVertexInIndex(mappedV0Index, graph.getVertices());
            Vertex v1Mapped = DegreeUtil.getVertexInIndex(mappedV1Index, graph.getVertices());

            if (!DegreeUtil.isEdgeBetween(v0Mapped, v1Mapped, vertexToNeighbors)) {
                return false;
            }
        }
        return true;
    }

    private Permutation permutation(OrderedPartition<Vertex> discretePartition) {
        Map<Integer, Integer> permutation = new HashMap<Integer, Integer>();
        for (int i = 0; i < discretePartition.getPartition().size(); i++) {
            List<Vertex> part = discretePartition.getPartition().get(i);
            Vertex v = part.get(0); //the only one
            Integer vertexIndex = DegreeUtil.indexOf(v,graph.getVertices());
            permutation.put(vertexIndex, i);
        }
        return new Permutation(permutation);
    }


    private int deg(Vertex v, List<Vertex> Vj) {
        int deg = 0;
        Set<Vertex> vertices = graph.getVertexToNeighbors().get(v);
        for (Vertex test : vertices) {
            if (Vj.contains(test))
                deg++;
        }
        return deg;
    }

}
