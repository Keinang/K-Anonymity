package App.Utils;

import App.Model.Graph;
import App.Model.Vertex;

import java.util.*;

/**
 * Created by Keinan.Gilad on 10/16/2016.
 */
public class BFS {
    public List<Vertex> bfs(Map<Vertex, Set<Vertex>> vertexToNeighbors, Vertex source) {
        List<Vertex> result = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        visited.add(source);
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex element = queue.remove();
            //System.out.println(element);
            result.add(element);
            Set<Vertex> vertices = vertexToNeighbors.get(element);
            for (Vertex vertex : vertices) {
                if (!visited.contains(vertex)) {
                    queue.add(vertex);
                    visited.add(vertex);
                }
            }
        }

        return result;
    }

    public static void main(String... arg) {
        DemoDataCreator creator = new DemoDataCreator();

        BFS bfs = new BFS();

        Graph graph = creator.generateGraphT3();
        bfs.bfs(graph.getVertexToNeighbors(), new Vertex("0"));

        Graph graph2 = creator.generateGraphT4();
        bfs.bfs(graph2.getVertexToNeighbors(), new Vertex("0"));
    }
}
