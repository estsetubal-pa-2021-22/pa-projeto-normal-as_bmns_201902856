package pt.pa.commands;

import pt.pa.Logger;
import pt.pa.graph.Edge;
import pt.pa.graph.Vertex;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Route;


public class CommandRemove extends Command{

    public CommandRemove(MainPane pane) {
        super(pane);
    }

    /**
     *Execute the removal of the edge from the graph(GUI) from string (element of vertex) and save the state
     */
    @Override
    public boolean execute() {
        String hub1 = pane.getNameHub1Value();
        Edge<Route,Hub> finale = null;
        String hub2 = pane.getNameHub2Value();
        Vertex<Hub> for1 =pane.getVertexByElemValue(hub1);
        Vertex<Hub> for2 = pane.getVertexByElemValue(hub2);
        if (for1 != null && for2 != null){
            if (pane.checkGraphAdjancyByVertex(for1, for2)){

                for (Edge<Route,Hub> e: pane.getGraphIncidentEdges(for1)) {
                    if (pane.getGraphOppositeVertex(for1,e) == for2){
                        finale = e;
                    }
                }
                Logger.getInstance().logInfo(pane,
                        String.format("Removed route:\n  from %s\n  to %s\n(Distance = %.2f)",
                                for1.element().getName(),
                                for2.element().getName(),
                                finale.element().getDistance()
                        )
                );
                pane.removeGraphEdge(finale);
                backup(finale);
                return true;
            }
        }
        Logger.getInstance().logError(pane,
                String.format("A route doesn't exist\n  from %s\n  to %s!",
                        for1.element().getName(),
                        for2.element().getName()
                )
        );
        return false;
    }

    /**
     * operation opposite to the execute, in this case add
     */
    @Override
    public void undo() {
        if(backup != null) {
            Logger.getInstance().logInfo(pane,
                    String.format("Undo of removed route:\n  from %s\n  to %s\n(Distance = %.2f)",
                            backup.element().getFirstHub().getName(),
                            backup.element().getSecondHub().getName(),
                            backup.element().getDistance()
                    )
            );
            pane.insertGraphEdgeWithHub(backup.element().getFirstHub(), backup.element().getSecondHub(), backup.element());
            pane.updateGraph();
        }
    }
}