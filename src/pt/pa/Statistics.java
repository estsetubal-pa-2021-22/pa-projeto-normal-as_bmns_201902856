package pt.pa;

import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;
import pt.pa.model.Hub;
import pt.pa.model.Route;
import pt.pa.shortcuts.StringFromIterable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds all the useful functions related to the graph's statistics.
 */
public class Statistics {
    private Graph<Hub, Route> graph;

    private boolean willLog;

    /**
     * @param graph Graph you want to get the statistics from.
     */
    public Statistics(Graph graph) {
        this.graph = graph;
        this.willLog = true;
    }

    public int getAmountOfHubs() {
        int hubs = graph.numVertices();
        if (willLog) {
            Logger.getInstance().logInfo(this,
                    String.format("Hub Count: %d", hubs)
            );
        }
        return hubs;
    }

    public int getAmountOfRoutes() {
        int routes = graph.numEdges();
        if (willLog) {
            Logger.getInstance().logInfo(this,
                    String.format("Route Count: %d", routes)
            );
        }
        return routes;
    }

    /**
     * Determines the amount of adjacent hubs of each hub, i.e., how centralized the hubs are.
     *
     * @return An ordered list with tuples containing the hub and the respective amount of adjacent hubs.
     */
    public List<HubWithAdjacents> getHubCentrality() {
        List<Vertex<Hub>> allVertices = new ArrayList<>(graph.vertices());

        List<HubWithAdjacents> allHubsWithAdjacents = new ArrayList<>();
        for(Vertex<Hub> vertex: allVertices) {
            HubWithAdjacents hubWithAdjacents = new HubWithAdjacents(vertex.element(), graph.getAdjacentVertices(vertex).size());
            allHubsWithAdjacents.add(hubWithAdjacents);
        }

        Collections.sort(allHubsWithAdjacents);

        if (willLog) {
            Logger.getInstance().logInfo(this,
                    String.format("Hub Centrality:\n\t%s",
                            (new StringFromIterable<HubWithAdjacents>()).delimit("\n\t", allHubsWithAdjacents)
                    )
            );
        }


        return allHubsWithAdjacents;
    }

    /**
     * @return The top 5 hubs with the most adjacent hubs.
     */
    public List<HubWithAdjacents> getTop5Hubs() {
        this.willLog = false;
        List<HubWithAdjacents> top5List = getHubCentrality().stream().limit(5).collect(Collectors.toList());
        this.willLog = true;

        Logger.getInstance().logInfo(this,
                String.format("Top 5 with Most Connections:\n %s",
                        (new StringFromIterable<HubWithAdjacents>()).delimit("\n ", top5List)
                )
        );


        return getHubCentrality().stream().limit(5).collect(Collectors.toList());
    }

    /**
     * It calculates how many subnetworks there are in the graph using the
     * Depth-First Search searching algorithm.
     *
     * @return Total amount of logistical subnetworks
     */
    public int amountOfLogisticalSubnetworks() {
        List<Vertex<Hub>> allVertices = new ArrayList<>(graph.vertices());
        List<Vertex<Hub>> allTraversedVertices = new ArrayList<>();

        int amountOfSubnetworks = 0;
        for(Vertex<Hub> v : allVertices) {
            if(allTraversedVertices.contains(v)) {
                continue;
            }

            List<Vertex<Hub>> traversedVertices = graph.depthFirstSearch(v);

            allTraversedVertices.addAll(traversedVertices);
            amountOfSubnetworks++;
        }

        if (willLog) {
            Logger.getInstance().logInfo(this,
                    String.format("Amount of Subnetworks: %d", amountOfSubnetworks
                    )
            );
        }

        return amountOfSubnetworks;
    }

    /**
     * Inner class whose purpose is to serve as a tuple, containing a Hub and the
     * respective amount of adjacent hubs.
     */
    public class HubWithAdjacents implements Comparable {
        private Hub hub;
        private int amountOfAdjacentHubs;

        public HubWithAdjacents(Hub hub, int amountOfAdjacentHubs){
            this.hub = hub;
            this.amountOfAdjacentHubs = amountOfAdjacentHubs;
        }

        @Override
        public int compareTo(Object o) {
            HubWithAdjacents hubWithAdjacents = (HubWithAdjacents) o;
            return Integer.compare(hubWithAdjacents.amountOfAdjacentHubs, amountOfAdjacentHubs);
        }

        public Hub getHub() {
            return hub;
        }

        public int getAmountOfAdjacentHubs() {
            return amountOfAdjacentHubs;
        }

        @Override
        public String toString() {
            return hub.getName() + " ---> " + amountOfAdjacentHubs + " adjacent hubs";
        }
    }
}
