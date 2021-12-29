package pt.pa.commands;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
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
        String hub1 = pane.nameHub1.getSelectedText().toLowerCase(Locale.ROOT);
        String hub2 = pane.nameHub2.getSelectedText().toLowerCase(Locale.ROOT);
        Vertex<Hub> for1 = null;
        Vertex<Hub> for2 = null;

        for (Vertex<Hub> v :pane.g.vertices()) {
            if (v.element().getName().toLowerCase(Locale.ROOT) == hub1){
                for1 = v;
            }
            if (v.element().getName().toLowerCase(Locale.ROOT) == hub2){
                for2 = v;
            }
        }
        if (for1 != null && for2 != null){
            if (!pane.g.areAdjacent(for1, for2)){
                backup();
                pane.g.insertEdge(for1, for2, new Route(for1.element(), for2.element()));
                return true;
            }
        }
        return false;
    }
}
