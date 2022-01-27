package pt.pa.commands;

import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Route;

public abstract class Command {
    public MainPane pane;
    protected Edge<Route, Hub> backup;

    Command(MainPane pane) {
        this.pane = pane;
    }

    /**
     * Save the actual state
     * @param e edge to save
     */
    void backup(Edge<Route, Hub> e) {
        backup = e;
    }

    /**
     *Operation to return to the last state
     */
    public abstract void undo();

    /**
     *Execute the action
     */
    public abstract boolean execute();
}