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

    void backup(Edge<Route, Hub> e) {
        backup = e;
    }

    public abstract void undo();

    public abstract boolean execute();
}
