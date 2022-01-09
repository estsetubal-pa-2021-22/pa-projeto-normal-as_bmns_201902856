package pt.pa.commands;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import pt.pa.Logger;
import pt.pa.graph.Edge;
import pt.pa.graph.Vertex;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.Locale;

public class CommandAdd extends Command{

    public CommandAdd(MainPane pane) {
        super(pane);
    }

    @Override
    public boolean execute() {
        String hub1 = (String) pane.getNameHub1().getValue();
        String hub2 = (String) pane.getNameHub2().getValue();
        Vertex<Hub> for1 = null;
        Vertex<Hub> for2 = null;
        for (Vertex<Hub> v :pane.g.vertices()) {
            if (v.element().getName().toLowerCase().equals(hub1.toLowerCase())){
                for1 = v;
            }
            if (v.element().getName().toLowerCase().equals(hub2.toLowerCase())){
                for2 = v;
            }
        }
        if (for1 != null && for2 != null){
            if (!pane.g.areAdjacent(for1, for2)){
                Edge<Route, Hub> e = pane.g.insertEdge(for1, for2, new Route(for1.element(), for2.element()));
                backup(e);
                Logger.getInstance().logInfo(pane,
                        String.format("Added route:\n  from %s\n  to %s\n(Distance = %.2f)",
                            for1.element().getName(),
                            for2.element().getName(),
                            e.element().getDistance()
                        )
                );
                return true;
            }
        }
        Logger.getInstance().logError(pane,
                String.format("A route already exists\n  from %s\n  to %s!",
                        for1.element().getName(),
                        for2.element().getName()
                )
        );
        return false;
    }

    @Override
    public void undo() {
        if(backup != null) {
            Edge<Route, Hub> aux = null;
            /*
             *check for because of case add - remove - undo - undo
             */
            for (Edge<Route, Hub> e:pane.g.edges()) {
                if (e.element().getFirstHub().equals(backup.element().getFirstHub()) && e.element().getSecondHub().equals(backup.element().getSecondHub())){
                    aux = e;
                }

            }
            if (aux!=null){
                Logger.getInstance().logInfo(pane,
                        String.format("Undo of added route:\n  from %s\n  to %s\n(Distance = %.2f)",
                                aux.element().getFirstHub().getName(),
                                aux.element().getSecondHub().getName(),
                                aux.element().getDistance()
                        )
                );
                pane.g.removeEdge(aux);
                pane.graphView.updateAndWait();
            }
        }
    }
}