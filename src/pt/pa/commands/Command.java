package pt.pa.commands;

import pt.pa.graph.Graph;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Route;

public abstract class Command {
    public MainPane pane;
    private Graph<Hub, Route> backup; //TODO Ver para algo mais leve

    Command(MainPane pane) {
        this.pane = pane;
    }

    void backup() {
        backup = pane.g;
    }

    public void undo() {
        pane.g = backup;
    }

    public abstract boolean execute();
}
