package pt.pa.dijkstra;

import javafx.util.Pair;
import pt.pa.graph.Edge;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.Vertex;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implemented using the Singleton design pattern.
 */
public class HubRouteDijkstra {
    private static HubRouteDijkstra instance;

    private static GraphAdjacencyList<Hub, Route> graph;

    private HubRouteDijkstra() {}

    public static HubRouteDijkstra getInstance() {
        if (instance == null) {
            instance = new HubRouteDijkstra();
        }
        return instance;
    }

    public void setGraph(GraphAdjacencyList<Hub, Route> graph) {
        this.graph = graph;
    }

    /**
     * Calculates a table using the dijkstra algorithm.
     * @param origin The vertex it start the search.
     * @param unit A boolean which determines if its a unit or not.
     * @return The final result, the dijkstra table.
     */
    public HubRouteDijkstraResult dijkstra(Vertex<Hub> origin, boolean unit) {
        HubRouteDijkstraResult output = new HubRouteDijkstraResult();
        Set<Vertex<Hub>> allVertices = new HashSet<>(graph.depthFirstSearch(origin));

        for (Vertex<Hub> v: allVertices) {
            output.setCost(v, Integer.MAX_VALUE);
            output.setPredecessor(v, null);
        }
        output.setCost(origin, 0);

        while (!allVertices.isEmpty()) {
            Vertex<Hub> lowest = findLowest(allVertices, output);
            if (output.getCost(lowest) == Integer.MAX_VALUE) return null;

            allVertices.remove(lowest);

            for (Vertex<Hub> v: graph.getAdjacentVertices(lowest)) {
                int cost = output.getCost(lowest) + getDistanceBetween(v, lowest, unit);
                if (cost < output.getCost(v)) {
                    output.setCost(v, cost);
                    output.setPredecessor(v, lowest);
                }
            }
        }

        return output;
    }

    public HubRouteDijkstraResult dijkstra(Vertex<Hub> origin) {
        return dijkstra(origin, false);
    }

    public HubRouteDijkstraResult unitDijkstra(Vertex<Hub> origin) {
        return dijkstra(origin, true);
    }

    public List<Vertex<Hub>> shortestPath(Vertex<Hub> hub1, Vertex<Hub> hub2) {

        return shortestPath(hub1, hub2, new AtomicInteger(0));
    }

    /**
     * Returns the shortest path between two vertices.
     * @param hub1 The vertex it starts the path.
     * @param hub2 The vertex it ends the path.
     * @param distance In case its relevant to know the cost.
     * @return List of vertices in the shortest path.
     */
    public List<Vertex<Hub>> shortestPath(Vertex<Hub> hub1, Vertex<Hub> hub2, AtomicInteger distance) {
        HubRouteDijkstraResult dijkstra = dijkstra(hub1);
        Vertex<Hub> goal = hub2;

        distance.set(dijkstra.getCost(hub2));

        List<Vertex<Hub>> path = new ArrayList<>();
        path.add(goal);

        if (hub1.equals(hub2)) return path;

        while (!goal.equals(hub1)) {
            goal = dijkstra.getPredecessor(goal);
            path.add(0, goal);
        }

        path.add(0, hub1);

        return path;
    }

    public List<Vertex<Hub>> shortestPath(HubRouteDijkstraResult dijkstra, Vertex<Hub> hub2) {

        return shortestPath(dijkstra, hub2, new AtomicInteger(0));
    }

    public List<Vertex<Hub>> shortestPath(HubRouteDijkstraResult dijkstra, Vertex<Hub> hub2, AtomicInteger distance) {

        Vertex<Hub> goal = hub2;

        //System.out.println("Init Path Vertex: " + hub2.element());

        distance.set(dijkstra.getCost(hub2));

        List<Vertex<Hub>> path = new ArrayList<>();
        path.add(goal);

        //if (goal.equals(hub2)) return path;

        while (!(dijkstra.getPredecessor(goal) == null)) {
            //System.out.println("Shortest Path Vertex: " + goal.element());
            goal = dijkstra.getPredecessor(goal);
            path.add(0, goal);
        }

        path.add(0, goal);

        return path;
    }

    /**
     * Returns a pair of vertices which are furthest from each other, while being the shortest path possible between them.
     * @return Pair of vertices furthest from each other.
     */
    public Pair<Vertex<Hub>, Vertex<Hub>> farthestHubPair() {
        Pair<Vertex<Hub>, Vertex<Hub>> pair = new Pair<>(null, null);

        List<Vertex<Hub>> vertices = new ArrayList<>(graph.vertices());
        int distance = 0;

        for (Vertex<Hub> v: vertices) {
            HubRouteDijkstraResult dijkstra = dijkstra(v);
            Vertex<Hub> farthest = dijkstra.farthestHub();
            int currentDistance = dijkstra.getCost(farthest);

            if (currentDistance > distance) {
                pair = new Pair<>(v, farthest);
                distance = currentDistance;
            }

        }

        return pair;
    }

    /**
     * Find the edges between vertices in a list of vertices.
     * @param vertices The list of vertices to be read.
     * @return The list of edges which are between each vertex.
     */
    public List<Edge<Route, Hub>> edgesUpTo(List<Vertex<Hub>> vertices) {
        List<Edge<Route, Hub>> edges = new ArrayList<>();
        //Vertex<Hub> current = vertices.get(vertices.size() - 1);
        for(int i = vertices.size() - 1; i > 0; i--) {
            Edge<Route, Hub> edge = null;
            for(Edge<Route, Hub> e: graph.incidentEdges(vertices.get(i))) {
                if (graph.opposite(vertices.get(i), e).equals(vertices.get(i-1))) {
                    edges.add(e);
                }
            }
        }

        return edges;
    }

    private Vertex<Hub> findLowest(Set<Vertex<Hub>> vSet, HubRouteDijkstraResult hrdr) {
        int cost = Integer.MAX_VALUE;
        Vertex<Hub> lowest = null;

        for (Vertex<Hub> v: vSet) {
            if (hrdr.getCost(v) < cost) {
                cost = hrdr.getCost(v);
                lowest = v;
            }
        }

        return lowest;
    }

    private int getDistanceBetween(Vertex<Hub> hub1, Vertex<Hub> hub2, boolean unit) {
        for (Edge<Route, Hub> e: graph.incidentEdges(hub1)) {
            if (graph.opposite(hub1, e).equals(hub2)) {
                return unit ? 1 : (int)(e.element().getDistance());
            }
        }
        return -1;
    }
}

