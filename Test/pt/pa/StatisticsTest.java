package pt.pa;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.Vertex;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.List;

import static org.junit.Assert.*;

public class StatisticsTest {
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
    Edge<Route, Hub> e9;

    Statistics stats;

    @Before
    public void setup() {
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
        e9 = distances.insertEdge(beijing, prague, new Route(beijing.element(), prague.element(), 99999));
        stats = new Statistics(distances);
    }

    @Test
    public void testGetAmountOfHubs() {
        int amountHubs = stats.getAmountOfHubs();
        assertTrue(amountHubs == 6);
    }

    @Test
    public void testGetAmountOfRoutes() {
        int amountRoutes = stats.getAmountOfRoutes();
        assertTrue(amountRoutes == 9);
    }

    @Test
    public void testGetHubCentralityAndOrder() {
        List<Statistics.HubWithAdjacents> allHubsWithAdjacents = stats.getHubCentrality();
        int amountAdjacents = -1;
        for(Statistics.HubWithAdjacents hubWithAdjacents: allHubsWithAdjacents) {
            if(amountAdjacents != -1) {
                assertTrue(hubWithAdjacents.getAmountOfAdjacentHubs() <= amountAdjacents);
            }
            amountAdjacents = hubWithAdjacents.getAmountOfAdjacentHubs();
        }
        assertTrue(allHubsWithAdjacents.get(0).getAmountOfAdjacentHubs() == 4);
        assertTrue(allHubsWithAdjacents.get(0).getHub().getName().equals("Beijing"));
        assertTrue(allHubsWithAdjacents.get(5).getAmountOfAdjacentHubs() == 2);
        assertTrue(allHubsWithAdjacents.get(5).getHub().getName().equals("Helsinky"));
    }

    @Test
    public void testGetTop5Hubs() {
        List<Statistics.HubWithAdjacents> top5 = stats.getTop5Hubs();
        assertTrue(top5.get(0).getAmountOfAdjacentHubs() == 4);
        assertTrue(top5.get(0).getHub().getName().equals("Beijing"));
        assertTrue(top5.get(4).getAmountOfAdjacentHubs() == 3);
        assertTrue(top5.get(4).getHub().getName().equals("New York"));
    }

    @Test
    public void testGetAmountOfLogisticalSubnetworks() {
        assertTrue(stats.amountOfLogisticalSubnetworks() == 1);
    }
}