package pt.pa.commands;

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
        String hub1 = pane.nameHub1.getText().toLowerCase();
        Vertex<Hub> for1 = null;
        Vertex<Hub> for2 = null;
        Edge<Route,Hub> finale = null;
        String hub2 = pane.nameHub2.getText().toLowerCase();
        for (Vertex<Hub> v :pane.g.vertices()) {
            if (v.element().getName().toLowerCase() == hub1){
                for1 = v;
            }
            if (v.element().getName().toLowerCase() == hub2){
                for2 = v;
            }
        }
        if (for1 != null && for2 != null){
            if (pane.g.areAdjacent(for1, for2)){
                backup();
                for (Edge<Route,Hub> e:pane.g.incidentEdges(for1)) {
                    if (pane.g.opposite(for1,e) == for2){
                        finale = e;
                    }
                }
                pane.g.removeEdge(finale);
                return true;
            }
        }
        return false;
    }
}
