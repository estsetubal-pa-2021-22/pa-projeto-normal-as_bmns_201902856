package pt.pa.dijkstra;

import pt.pa.graph.Vertex;
import pt.pa.model.Hub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HubRouteDijkstraResult {
    private Map<Vertex<Hub>, Integer> costs;
    private Map<Vertex<Hub>, Vertex<Hub>> predecessors;

    public HubRouteDijkstraResult() {
        this.costs = new HashMap<>();
        this.predecessors = new HashMap<>();
    }

    public Vertex<Hub> farthestHub() {
        AtomicInteger cost = new AtomicInteger(0);
        AtomicReference<Vertex<Hub>> vertex = new AtomicReference<>(null);

        costs.forEach((v, c) -> {
            if (c > cost.get()) {
                cost.set(c);
                vertex.set(v);
            }
        });

        return vertex.get();
    }

    public void setCost(Vertex<Hub> vertex, int cost) { costs.put(vertex, cost); }
    public void setPredecessor(Vertex<Hub> vertex, Vertex<Hub> prev) { predecessors.put(vertex, prev); }

    public int getCost(Vertex<Hub> vertex) { return costs.get(vertex); }
    public Vertex<Hub> getPredecessor(Vertex<Hub> vertex) { return predecessors.get(vertex); }


}

