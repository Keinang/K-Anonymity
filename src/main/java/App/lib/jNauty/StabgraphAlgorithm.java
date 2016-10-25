package App.lib.jNauty;

import App.Common.Utils.DegreeUtil;
import App.Model.Edge;
import App.Model.Graph;
import App.Model.Vertex;
import org.apache.commons.math3.util.Pair;

import java.util.*;

/**
 * Created by Keinan.Gilad on 10/22/2016.
 */
public class StabgraphAlgorithm {
    public List<List<Vertex>> getCyclicRepresenatation(Graph graph) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();

        // create ordered partition
        OrderedPartition<Vertex> pi = getVertexDegreesPartition(vertexToNeighbors);
        // refinement
        OrderedPartition<Vertex> refined = refinementProcedure(pi, vertexToNeighbors);
        return refined.getPartition();
    }

    private OrderedPartition<Vertex> getVertexDegreesPartition(Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        Map<Integer, List<Vertex>> degreeToVertices = new HashMap<>();
        Iterator<Vertex> vertexIterator = vertexToNeighbors.keySet().iterator();
        while (vertexIterator.hasNext()) {
            Vertex vertex = vertexIterator.next();
            Set<Vertex> neighbors = vertexToNeighbors.get(vertex);
            int degree = neighbors.size();
            List<Vertex> degreeVertices = degreeToVertices.get(degree);
            if (degreeVertices == null) {
                // fist to come in this degree
                degreeVertices = new ArrayList<>();
            }
            degreeVertices.add(vertex);
            degreeToVertices.put(degree, degreeVertices);
        }

        // sorting the degree ascending
        Set<Integer> degrees = degreeToVertices.keySet();
        List<Integer> degreesList = new ArrayList(degrees);
        Collections.sort(degreesList);

        // creating the ordered partition
        List<List<Vertex>> result = new ArrayList<>();
        for (Integer deg : degreesList) {
            result.add(degreeToVertices.get(deg));
        }

        return new OrderedPartition(result);
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

    private OrderedPartition<Vertex> refinementProcedure(OrderedPartition<Vertex> pi, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        OrderedPartition<Vertex> tau = new OrderedPartition<Vertex>(pi.getPartition());
        List<Pair<List<Vertex>, List<Vertex>>> B = new ArrayList<Pair<List<Vertex>, List<Vertex>>>();
        Map<Integer, List<Vertex>> degreesMap = new HashMap<Integer, List<Vertex>>();

        int maxLoop = 5;
        int counter = 0;
        while (counter < maxLoop) {
            counter++;
            //logger.debug("refinementProcedure - Iteration start");
            B.clear();
            List<List<Vertex>> partition = tau.getPartition();
            List<List<Vertex>> partition2 = tau.getPartition();
            for (int i = 0; i < partition.size(); i++) {
                List<Vertex> Vi = partition.get(i);

                for (int j = 0; j < partition2.size(); j++) {
                    //check if Vj shatters Vi
                    List<Vertex> Vj = partition2.get(j);

                    if (isDegreeShatters(Vi, Vj, vertexToNeighbors)) {
                        B.add(new Pair(Vi, Vj));
                        break;
                    }
                }
            }

            if (B.size() == 0) {
                break;
            }

            //now find the minimum element
            //logger.debug("refinementProcedure - find the minimum element");
            Pair<List<Vertex>, List<Vertex>> minimalPair = findMinimal(B, vertexToNeighbors);

            //now replace Vi with X1,X2,...Xt
            //logger.debug("refinementProcedure - replace Vi with X1,X2,...Xt");
            degreesMap.clear();
            List<Vertex> Vi = minimalPair.getKey();
            List<Vertex> Vj = minimalPair.getValue();

            List<Integer> degrees = new ArrayList<Integer>();
            for (int i = 0; i < Vi.size(); i++) {
                Integer deg = getRetainDegree(Vi, i, Vj, vertexToNeighbors);
                List<Vertex> verticesWithDegree = degreesMap.get(deg);
                if (verticesWithDegree == null) {
                    verticesWithDegree = new ArrayList<Vertex>();
                    degreesMap.put(deg, verticesWithDegree);
                }
                verticesWithDegree.add(Vi.get(i));
                if (!degrees.contains(deg)) {
                    degrees.add(deg);
                }
            }

            // sort to insert those with lower degrees first
            //logger.debug("refinementProcedure - sort");
            Collections.sort(degrees);

            //logger.debug("refinementProcedure - replacements");
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
        //logger.debug("findMinimal - Start");
        Pair<List<Vertex>, List<Vertex>> minimalPair = null;
        String minimalBinary1 = null;
        String minimalBinary2 = null;

        for (Pair<List<Vertex>, List<Vertex>> separationPair : B) {
            //logger.debug("findMinimal - iteration");
            //logger.debug("findMinimal - start binaryRepresenatation for binary1");
            String binary1 = BinaryRepresentation.binaryRepresenatation(separationPair.getKey(), vertexToNeighbors);
            //logger.debug("findMinimal - done binaryRepresenatation for binary1");

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
                    if (minimalBinary2 == null) {
                        //logger.debug("findMinimal - start binaryRepresenatation for minimalBinary2");
                        minimalBinary2 = BinaryRepresentation.binaryRepresenatation(minimalPair.getValue(), vertexToNeighbors);
                        //logger.debug("findMinimal - done binaryRepresenatation for minimalBinary2");
                    }

                    //logger.debug("findMinimal - start binaryRepresenatation for binary2");
                    String binary2 = BinaryRepresentation.binaryRepresenatation(separationPair.getValue(), vertexToNeighbors);
                    //logger.debug("findMinimal - done binaryRepresenatation for binary2");
                    if (binary2.compareTo(minimalBinary2) <= 0) {
                        minimalPair = separationPair;
                        minimalBinary1 = binary1;
                        minimalBinary2 = binary2;
                    }

                }
            }
        }
        //logger.debug("findMinimal - Done");
        //System.out.println("Minimal pair is: " + minimalPair.getKey() + " " + minimalPair.getValue());
        return minimalPair;
    }

    private boolean isDegreeShatters(List<Vertex> Vi, List<Vertex> Vj, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        // Degree of first Vertex in Vi
        int deg = getRetainDegree(Vi, 0, Vj, vertexToNeighbors);

        // iterate Vi to
        for (int m = 1; m < Vi.size(); m++) {
            int degM = getRetainDegree(Vi, m, Vj, vertexToNeighbors);

            if (degM != deg) {
                return true;
            }
        }

        return false;
    }

    private int getRetainDegree(List<Vertex> Vi, int idx, List<Vertex> Vj, Map<Vertex, Set<Vertex>> vertexToNeighbors) {
        Set<Vertex> v0Neighbors = vertexToNeighbors.get(Vi.get(idx));
        int deg = 0;
        for (Vertex vNeighbor : v0Neighbors) {
            if (Vj.contains(vNeighbor)) {
                deg++;
            }
        }

        return deg;
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
        if (firstNontrivialrPart == null) {
            return;
        }

        //System.out.println("Current partition: " + currentPartition);
        for (Vertex u : firstNontrivialrPart) {
            //System.out.println("Splitting by " + u);
            OrderedPartition<Vertex> partition = splitPartition(u, currentPartition);
            partition = refinementProcedure(partition, vertexToNeighbors);
            //System.out.println(partition);
            new SearchTreeNode<Vertex>(partition, u, currentNode);
        }
        List<SearchTreeNode<Vertex>> children = currentNode.getChildren();
        for (SearchTreeNode<Vertex> node : children) {
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
        List<Permutation> ret = new ArrayList<Permutation>();

        //calculate permutation
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

}
