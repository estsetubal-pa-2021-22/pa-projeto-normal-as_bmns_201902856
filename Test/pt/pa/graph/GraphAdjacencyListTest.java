package pt.pa.graph;


import com.brunomnsilva.smartgraph.example.Distance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphAdjacencyListTest {
    Graph<Hub, Route> distances;
    Vertex<Hub> prague;
    Vertex<Hub> tokyo;
    Vertex<Hub> beijing;
    Vertex<Hub> newYork;
    Vertex<Hub> london;
    Vertex<Hub> helsinky;
    Edge<Route, Hub> e1;
    Edge<Route, Hub> e2;
    Edge<Route, Hub> e3;
    Edge<Route, Hub> e4;
    Edge<Route, Hub> e5;
    Edge<Route, Hub> e6;
    Edge<Route, Hub> e7;
    Edge<Route, Hub> e8;

    @BeforeEach
    void setup() {
        distances = new GraphAdjacencyList<>();
        prague = distances.insertVertex(new Hub("Prague", 0, 69, 420));
        tokyo = distances.insertVertex(new Hub("Tokyo", 0, 420, 69));
        beijing = distances.insertVertex(new Hub("Beijing", 0, 69, 69));
        newYork = distances.insertVertex(new Hub("New York", 0, 420, 420));
        london = distances.insertVertex(new Hub("London", 0, 42, 69));
        helsinky = distances.insertVertex(new Hub("Helsinky", 0, 69, 42));
        e1 = distances.insertEdge(tokyo, newYork, new Route(tokyo.element(), newYork.element(), 10838));
        e2 = distances.insertEdge(beijing, newYork, new Route(beijing.element(), newYork.element(), 11550));
        e3 = distances.insertEdge(beijing, tokyo, new Route(beijing.element(), tokyo.element(), 1303));
        e4 = distances.insertEdge(london, newYork, new Route(london.element(), newYork.element(), 5567));
        e5 = distances.insertEdge(london, prague, new Route(london.element(), prague.element(), 1264));
        e6 = distances.insertEdge(helsinky, tokyo, new Route(helsinky.element(), tokyo.element(), 7815));
        e7 = distances.insertEdge(prague, helsinky, new Route(prague.element(), helsinky.element(), 1845));
        e8 = distances.insertEdge(beijing, london, new Route(beijing.element(), london.element(), 8132));
    }

    @Test
    void numVertices() {
        assertEquals(6, distances.numVertices());
    }

    @Test
    void numEdges() {
        assertEquals(8, distances.numEdges());
    }

    @Test
    void areAdjacent() {
        assertTrue(distances.areAdjacent(tokyo, newYork));
    }

    @Test
    void opposite() {
        assertEquals(newYork, distances.opposite(tokyo, e1));
    }

    @org.junit.jupiter.api.Test
    void vertices() {
        List<Vertex<Hub>> vertexList = new ArrayList<>(distances.vertices());

        assertTrue(vertexList.contains(prague) && vertexList.contains(tokyo) && vertexList.contains(beijing)
                && vertexList.contains(newYork) && vertexList.contains(london) && vertexList.contains(helsinky));
    }

    @org.junit.jupiter.api.Test
    void edges() {
        List<Edge<Route, Hub>> edgeList = new ArrayList<>(distances.edges());

        assertTrue(edgeList.contains(e1) && edgeList.contains(e2) && edgeList.contains(e3) && edgeList.contains(e4)
                && edgeList.contains(e5) && edgeList.contains(e6) && edgeList.contains(e7) && edgeList.contains(e8));
    }

    @org.junit.jupiter.api.Test
    void incidentEdges() {
        List<Edge<Route, Hub>> edgeList = (ArrayList<Edge<Route, Hub>>) distances.incidentEdges(prague);

        assertTrue(edgeList.contains(e5) && edgeList.contains(e7));
    }

    @org.junit.jupiter.api.Test
    void insertVertex() {
        Vertex<Hub> porto;
        porto = distances.insertVertex(new Hub("Porto", 0, 499, 99));

        List<Vertex<Hub>> vertexList = new ArrayList<>(distances.vertices());
        assertTrue(vertexList.contains(porto));

    }

    @org.junit.jupiter.api.Test
    void insertEdge() {
        Edge<Route, Hub> e9;
        e9 = distances.insertEdge(tokyo, newYork, new Route(tokyo.element(), newYork.element(), 10838));

        List<Edge<Route, Hub>> edgeList = new ArrayList<>(distances.edges());
        assertTrue(edgeList.contains(e9));
    }


    @org.junit.jupiter.api.Test
    void removeVertex() {
        distances.removeVertex(prague);

        List<Vertex<Hub>> vertexList = new ArrayList<>(distances.vertices());
        for (Vertex<Hub> vertex : vertexList) {
            System.out.println(vertex.element());
        }
        assertFalse(vertexList.contains(prague));
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        distances.removeEdge(e1);

        List<Edge<Route, Hub>> edgeList = new ArrayList<>(distances.edges());
        assertFalse(edgeList.contains(e1));
    }

    @org.junit.jupiter.api.Test
    void replaceVertex() {
        Hub newHub = new Hub("Porto", 0, 10, 45);
        distances.replace(prague, newHub);

        List<Vertex<Hub>> vertexList = new ArrayList<>(distances.vertices());

        for (Vertex<Hub> vertex : vertexList) {
            if (vertex.equals(prague)) {
                assertEquals(newHub, vertex.element());
                break;
            }
        }

    }

    @org.junit.jupiter.api.Test
    void replaceEdge() {
        Route newRout = new Route(london.element(), prague.element(), 1264);
        distances.replace(e1, newRout);

        List<Edge<Route, Hub>> edgeList = new ArrayList<>(distances.edges());
        for (Edge<Route, Hub> edge : edgeList) {
            if (edge.equals(e1)) {
                assertEquals(newRout, e1.element());
                break;
            }
        }
    }

    @org.junit.jupiter.api.Test
    void test_areAdjacent_InvalidVertexException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        assertThrows(InvalidVertexException.class, () -> {
            distances.areAdjacent(porto, prague);
        });
    }

    @org.junit.jupiter.api.Test
    void test_incidentEdges_InvalidVertexException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        assertThrows(InvalidVertexException.class, () -> {
            distances.incidentEdges(porto);
        });
    }

    @org.junit.jupiter.api.Test
    void test_opposite_InvalidVertexException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        assertThrows(InvalidVertexException.class, () -> {
            distances.opposite(porto, e1);
        });
    }

    @org.junit.jupiter.api.Test
    void test_opposite_InvalidEdgeException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        Edge<Route, Hub> e9;
        e9 = secondGraph.insertEdge(porto, porto, new Route(porto.element(), porto.element(), 8132));
        assertThrows(InvalidEdgeException.class, () -> {
            distances.opposite(prague, e9);
        });
    }

    @org.junit.jupiter.api.Test
    void test_insertVertex_InvalidVertexException() {
        assertThrows(InvalidVertexException.class, () -> {
            distances.insertVertex(prague.element());
        });
    }

    @org.junit.jupiter.api.Test
    void test_insertEdge_InvalidVertexException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        assertThrows(InvalidVertexException.class, () -> {
            distances.insertEdge(porto, newYork, new Route(tokyo.element(), newYork.element(), 10838));
        });
    }

    @org.junit.jupiter.api.Test
    void test_insertEdge_InvalidEdgeException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        assertThrows(InvalidEdgeException.class, () -> {
            distances.insertEdge(prague, newYork, null);
        });
    }

    @org.junit.jupiter.api.Test
    void test_removeVertex_InvalidVertexException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        assertThrows(InvalidVertexException.class, () -> {
            distances.removeVertex(porto);
        });
    }

    @org.junit.jupiter.api.Test
    void test_removeEdge_InvalidEdgeException() {
        Vertex<Hub> porto;
        Graph<Hub, Route> secondGraph;
        secondGraph = new GraphAdjacencyList<>();
        porto = secondGraph.insertVertex(new Hub("Porto", 0, 69, 420));
        Edge<Route, Hub> e9;
        e9 = secondGraph.insertEdge(porto, porto, new Route(porto.element(), porto.element(), 8132));
        assertThrows(InvalidEdgeException.class, () -> {
            distances.removeEdge(e9);
        });
    }

    @org.junit.jupiter.api.Test
    void test_replaceVertex_InvalidVertexException() {
        assertThrows(InvalidVertexException.class, () -> {
            distances.replace(prague, tokyo.element());
        });
    }

    @org.junit.jupiter.api.Test
    void test_replaceEdge_InvalidVertexException() {
        assertThrows(InvalidVertexException.class, () -> {
            distances.replace(e1, e2.element());
        });
    }
}