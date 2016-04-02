/**
 * This class represents a non-deterministic Buchi automaton (NBA).
 * It includes a method to construct an automaton from a graphviz graph.
 */
public class Buchi {
    private Set<State> states;
    private Set<Symbol> alphabet;
    private Map<Transition, Set<State>> transitionFunc;
    private Set<State> finalStates;
    // TODO State and Symbol are placeholders until we decide on concrete types
    // Could be string/char, or, depending on the format we get, might be smarter to use ids

    /**
     * Interprets a graphviz graph into an automaton.
     * TODO
     */
    public Buchi(String graphviz) {
        // TODO
    }
}
