package pt.pa.graph;

import pt.pa.dijkstra.HubRouteDijkstra;
import pt.pa.dijkstra.HubRouteDijkstraResult;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.*;

public class HubRouteGraphSearch {
    private GraphAdjacencyList<Hub, Route> graph;

    public HubRouteGraphSearch(GraphAdjacencyList<Hub, Route> graph) {
        this.graph = graph;
    }

    /**
     * Returns all vertices that are 'limit' edges away from 'root'.
     * @param root
     * @param limit
     * @return
     */
    public List<Hub> bfsLimited(Vertex<Hub> root, int limit) {

        HubRouteDijkstra.getInstance().setGraph(graph);
        HubRouteDijkstraResult dijkstra = HubRouteDijkstra.getInstance().unitDijkstra(root);

        List<Hub> returnList = new ArrayList<>();

        Queue<Vertex<Hub>> queue = new LinkedList<>();

        Set<Vertex<Hub>> visited = new HashSet<>();
        visited.add(root);

        queue.offer(root);

        while (!queue.isEmpty()) {
            Vertex<Hub> vertex = queue.poll();

            int routeDistance = HubRouteDijkstra.getInstance().shortestPath(dijkstra, vertex).size() - 1;

            if (routeDistance <= limit) {
                returnList.add(vertex.element());
            }

            for (Vertex<Hub> adjacent: graph.getAdjacentVertices(vertex)) {
                if (!visited.contains(adjacent)) {
                    visited.add(adjacent);
                    queue.offer(adjacent);
                }
            }
        }

        return (returnList.isEmpty()) ? null : returnList;
    }
}
