import java.util.*;
import java.util.stream.*;

class BuchiState extends State {
    final boolean isFinal;
    Map<Character, Set<String>> transitions = new HashMap<>(); // Maps to ids of next states

    public BuchiState(String id, boolean isInitial, boolean isFinal) {
        super(id, isInitial);
        this.isFinal = isFinal;
    }

    // TODO add hashcode and equals

    /**
     * Returns string representing state in graphviz format
     */
    public String toString() {
        return id + " " + "[label=\"" + (isInitial ? "*" : "") + id + (isFinal ? "$" : "") + "\"]";
    }

    /**
     * Returns string representing the set of transitions in graphviz format
     */
    public String transitionToString(Character nextChar) {
        return transitions.get(nextChar).stream()
            .map(s -> "\t" + id + " -> " + s + " [label=" + nextChar + "]")
            .collect(Collectors.joining("\n"));
    }

    /**
     * Returns representation of all transitions from this state
     */
    public String transitionsToString() {
        return transitions.entrySet().stream()
            .map(e -> transitionToString(e.getKey()))
            .collect(Collectors.joining("\n"));
    }
}
