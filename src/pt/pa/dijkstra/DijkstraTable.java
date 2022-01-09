package pt.pa.dijkstra;

import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.Vertex;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.Collections;
import java.util.HashSet;

public class DijkstraTable {

    private GraphAdjacencyList<Hub, Route> graph;
    private HashSet<DijkstraTuple> content;
    private DijkstraTuple origin;

    public DijkstraTable(Vertex<Hub> originVertex, GraphAdjacencyList<Hub, Route> graph) {
        this.content = new HashSet<>();
        this.graph = graph;
        this.origin = new DijkstraTuple(originVertex);
        this.content.add(origin);
    }

    public DijkstraTuple tupleFromVertex(Vertex<Hub> v) {
        for (DijkstraTuple d: content) {
            if (d.vertex.equals(v)) return d;
        }
        return null;
    }
    
    public DijkstraTuple getTupleOfGreatestCost() {
        return Collections.max(content, DijkstraTuple::compareTo);
    }

    private class DijkstraTuple implements Comparable<DijkstraTuple> {
        Vertex<Hub> vertex;
        int           cost;
        Vertex<Hub> predecessor;
        DijkstraTuple prev;

        public DijkstraTuple(Vertex<Hub> vertex, int cost, Vertex<Hub> predecessor) {
            this.vertex = vertex;
            this.cost = cost;
            this.predecessor = predecessor;
            this.prev = null;
        }

        public DijkstraTuple(Vertex<Hub> vertex, Vertex<Hub> predecessor) {
            this(vertex, Integer.MAX_VALUE, predecessor);
        }

        public DijkstraTuple(Vertex<Hub> vertex) {
            this(vertex, null);
        }

        public void setPrevious(DijkstraTuple dt) {this.prev = dt;}

        @Override
        public int compareTo(DijkstraTuple o) {
            return this.cost - o.cost;
        }
    }
}