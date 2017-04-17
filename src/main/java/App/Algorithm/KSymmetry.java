package App.Algorithm;

import App.Model.Graph;
import App.Model.Vertex;
import App.lib.jNauty.StabgraphAlgorithm;
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
    protected StabgraphAlgorithm stabgraphAlgorithm;

    @Override
    public Graph anonymize(Graph graph, Integer k) {
        // 1. fetch orbits from the graph by stabgraphAlgorithm algorithm (McKay).
        logger.debug("Start to findAutomorphisms");
        List<List<Vertex>> orbits = stabgraphAlgorithm.getCyclicRepresenatation(graph);
        if (orbits == null) {
            logger.debug("No orbits found");
            return graph;
        }
        List<List<Vertex>> anonymizedOrbits = new ArrayList<>();
        logger.debug(String.format("found %s orbits", orbits.size()));

        // 2. for each orbit -> call ocp until size at least k.
        for (int i = 0; i < orbits.size(); i++) {
            logger.debug(String.format("Iteration for orbit %s", i));

            List<Vertex> orbit = orbits.get(i);
            if (orbit.size() >= k) {
                /*for (Vertex v: orbit){
                    logger.debug("Vertex not copied: " + v);
                }*/
                logger.debug(String.format("orbit %s size above k", i));
                anonymizedOrbits.add(orbit);
                continue;
            }
            // orbit size is below k, calling ocp procedure.
            int copyCounter = 1;
            while (orbit.size() < k) {
                logger.debug(String.format("Start orbitCopying for orbit %s", i));
                orbit = orbitCopying(graph, orbit, copyCounter);
                copyCounter++;
                logger.debug(String.format("Done orbitCopying for orbit %s", i));
            }
            anonymizedOrbits.add(orbit);
        }

        // 3. return the anonymized graph
        logger.debug("return the anonymized graph");
        graph.setPartitions(anonymizedOrbits);
        return graph;
    }

    /**
     * Orbit copying
     *
     * @param graph - the graph
     * @param orbit - the orbit that will be copied.
     */
    private List<Vertex> orbitCopying(Graph graph, List<Vertex> orbit, int copyCounter) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = graph.getVertexToNeighbors();
        List<Vertex> orbitTemp = new ArrayList<>(orbit);
        // 1. for each vertex in orbit
        for (Vertex v : orbit) {
            // 1.1. introduce new vertex into the graph and add to orbit
            List<Vertex> vertices = graph.getVertices();

            //Vertex v = vertices.get(idx);
            String name = v.getName();
            if (name.startsWith("-")) {
                continue; // not copying tag vertices, only originals.
            }

            String vertexTagName = createVertexTagName(name, copyCounter);
            Vertex vTag = new Vertex(vertexTagName);
            // add vertex into the graph
            graph.addVertex(vTag);

            // add vertex into the orbit
            orbitTemp.add(vTag);//vertices.indexOf(vTag));

            // 1.2. connect new edges according to orbits.
            Set<Vertex> vertexNeighbors = vertexToNeighbors.get(v);
            for (Vertex neighbor : vertexNeighbors) {
                if (isInSameOrbit(neighbor, vertices, orbit)) {
                    // in same orbit connecting them by tag
                    graph.addEdge(vTag, new Vertex(createVertexTagName(neighbor.getName(), copyCounter)));
                } else {
                    // in other orbit connecting them regularly
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
    private boolean isInSameOrbit(Vertex neighbor, List<Vertex> vertices, List<Vertex> orbit) {
        int neighborIdx = vertices.indexOf(neighbor);
        if (neighborIdx < 0) {
            return false;
        }
        boolean isContains = orbit.contains(neighborIdx);
        return isContains;
    }

    /**
     * calculating the tag name
     *
     * @param name - vertex name
     * @return the name with tag (* -1)
     */
    private String createVertexTagName(String name, int copyCounter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < copyCounter; i++) {
            sb.append("-");
        }

        return sb.append(name).toString();
    }
}
