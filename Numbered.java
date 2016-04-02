/**
 * Represents a deterministic numbered automaton (DNA).
 */
public class Numbered {
    private Set<State> states;
    private Set<Symbol> alphabet;
    private Map<Transition, State> transitionFunc;
    private Set<State> finalStates;
    private Map<Transition, int> numbering;

    /**
     * Determinizes Buchi automaton according to algorithm shown in class.
     */
    public Numbered(Buchi buchi) {
        // TODO
    }

    /**
     * Prints the automaton in graphviz format.
     * TODO specify sorting
     */
    public String toString() {
        // TODO
    }
}
