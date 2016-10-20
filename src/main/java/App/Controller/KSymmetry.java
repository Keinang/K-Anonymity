package App.Controller;

import App.Common.IAlgorithm;
import App.Common.Utils.DegreeUtil;
import App.Common.Utils.DemoDataCreator;
import App.Model.Graph;
import App.Model.Vertex;
import App.lib.jNauty.McKayGraphLabelingAlgorithm;
import App.lib.jNauty.Permutation;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Keinan.Gilad on 10/20/2016.
 */
public class KSymmetry implements IAlgorithm {
    private static Logger logger = Logger.getLogger(KSymmetry.class);

    @Autowired
    private McKayGraphLabelingAlgorithm jNauty;

    @Override
    public Graph anonymize(Graph graph, Integer k) {
        // 1. fetch orbits from the graph by jNauty algorithm (McKay).
        logger.debug("Start to findAutomorphisms");
        List<Permutation> automorphisms = jNauty.findAutomorphisms(graph);
        logger.debug("Done to findAutomorphisms");
        if (automorphisms == null || automorphisms.size() == 0) {
            logger.debug("No orbits found");
            return graph;
        }

        // 1.1 take the last automorphism and fetch his orbits
        Permutation p = automorphisms.get(automorphisms.size() - 1);
        List<List<Integer>> orbits = p.cyclicRepresenatation();
        logger.debug(String.format("found %s orbits", orbits.size()));

        // keeping the vertices to do indexOf later on
        Set<Vertex> verticesPermutation = graph.getVertices();

        // 2. for each orbit -> call ocp until size at least k.
        for (int i=0; i<orbits.size(); i++) {
            logger.debug(String.format("Iteration for orbit %s", i));

            List<Integer> orbit = orbits.get(i);
            if (orbit.size() >= k) {
                logger.debug(String.format("orbit %s size above k", i));
                continue;
            }
            // orbit size is below k, calling ocp procedure.
            while (orbit.size() < k) {
                logger.debug(String.format("Start orbitCopying for orbit %s", i));
                orbit = orbitCopying(graph, orbit);
                logger.debug(String.format("Done orbitCopying for orbit %s", i));
            }
        }

        // 3. return the anonymized graph
        logger.debug("return the anonymized graph");
        return graph;
    }

    /**
     * Orbit copying
     *
     * @param graph - the graph
     * @param orbit - the orbit that will be copied.
     */
    private List<Integer> orbitCopying(Graph graph, List<Integer> orbit) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();
        List<Integer> orbitTemp = new ArrayList<>(orbit);
        // 1. for each vertex in orbit
        for (Integer idx : orbit) {
            // 1.1. introduce new vertex into the graph and add to orbit
            Set<Vertex> vertices = graph.getVertices();

            Vertex v = DegreeUtil.getVertexInIndex(idx, vertices);
            String vertexTagName = getVertexTagName(v.getName());
            Vertex vTag = new Vertex(vertexTagName);
            // add vertex into the graph
            vertices.add(vTag);

            // add vertex into the orbit
            orbitTemp.add(DegreeUtil.indexOf(vTag, vertices));

            // 1.2. connect new edges according to orbits.
            Set<Vertex> vertexNeighbors = vertexToNeighbors.get(v);
            for (Vertex neighbor : vertexNeighbors) {
                if (isInSameOrbit(neighbor, vertices, orbit)) {
                    // in same orbit connecting the them (by tag)
                    graph.addEdge(vTag, new Vertex(getVertexTagName(neighbor.getName())));
                } else {
                    // in same orbit connecting the them (regular)
                    graph.addEdge(vTag, neighbor);
                }
            }
        }

        return orbitTemp;
    }

    /**
     * @param neighbor - a neighbor we want to check if he part of the orbit
     * @param vertices - all graph's vertices
     * @param orbit    - an orbit to check in
     * @return true if neighbor is part of the orbit, false otherwise.
     */
    private boolean isInSameOrbit(Vertex neighbor, Set<Vertex> vertices, List<Integer> orbit) {
        int neighborIdx = DegreeUtil.indexOf(neighbor, vertices);
        boolean isContains = orbit.contains(neighborIdx);
        return isContains;
    }

    /**
     * calculating the tag name
     *
     * @param name - vertex name
     * @return the name with tag (* -1)
     */
    private String getVertexTagName(String name) {
        return String.format("-%s", name);
    }

    public static void main(String[] args){
        BasicConfigurator.configure();

        KSymmetry algo = new KSymmetry();
        algo.jNauty = new McKayGraphLabelingAlgorithm();

        // k=2
        Graph anonymize = algo.anonymize(DemoDataCreator.generateGraphSymmetry(), 2);
        System.out.println(anonymize.getVertices().size());
        System.out.println(anonymize.getVertices());

        // k=3
        anonymize = algo.anonymize(DemoDataCreator.generateGraphSymmetry(), 3);
        System.out.println(anonymize.getVertices().size());
        System.out.println(anonymize.getVertices());
    }
}
