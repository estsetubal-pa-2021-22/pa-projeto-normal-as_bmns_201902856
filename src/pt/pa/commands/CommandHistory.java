package pt.pa.commands;

import java.util.Stack;

public class CommandHistory {
    private Stack<Command> history = new Stack<>();

    /**
     * Save command in history
     * @param c command to save in history
     */
    public void push(Command c) {
        history.push(c);
    }

    /**
     * Used to make the undo
     * @return get last command used
     */
    public Command pop() {
        return history.pop();
    }

    /**
     * In case the stack is empty is impossible to undo to previous state
     * @return
     */
    public boolean isEmpty() { return history.isEmpty(); }
}