package pt.pa.commands;

import pt.pa.Logger;
import pt.pa.graph.Edge;
import pt.pa.graph.Vertex;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.Locale;

public class CommandRemove extends Command{

    public CommandRemove(MainPane pane) {
        super(pane);
    }

    @Override
    public boolean execute() {
        String hub1 = (String) pane.getNameHub1().getValue();
        Vertex<Hub> for1 = null;
        Vertex<Hub> for2 = null;
        Edge<Route,Hub> finale = null;
        String hub2 = (String) pane.getNameHub2().getValue();
        for (Vertex<Hub> v :pane.g.vertices()) {
            if (v.element().getName().toLowerCase().equals(hub1.toLowerCase())){
                for1 = v;
            }
            if (v.element().getName().toLowerCase().equals(hub2.toLowerCase())){
                for2 = v;
            }
        }
        if (for1 != null && for2 != null){
            if (pane.g.areAdjacent(for1, for2)){

                for (Edge<Route,Hub> e: pane.g.incidentEdges(for1)) {
                    if (pane.g.opposite(for1,e) == for2){
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
                pane.g.removeEdge(finale);
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
            pane.g.insertEdge(backup.element().getFirstHub(), backup.element().getSecondHub(), backup.element());
            pane.graphView.updateAndWait();
        }
    }
}