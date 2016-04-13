import java.util.*;
import java.util.stream.*;

class BuchiState extends State {
    final boolean isFinal;
    Map<Character, Set<String>> transitions = new HashMap<>(); // Maps to ids of next states

    public BuchiState(String id, boolean isInitial, boolean isFinal) {
        super(id, isInitial);
        this.isFinal = isFinal;
    }

    @Override
    public String toString() {
        return id + " " + "[label=\"" + (isInitial ? "*" : "") + id + (isFinal ? "$" : "") + "\"]";
    }

    @Override
    public String transitionToString(Character nextChar) {
        return transitions.get(nextChar).stream()
            .map(s -> "\t" + id + " -> " + s + " [label=" + nextChar + "]")
            .collect(Collectors.joining("\n"));
    }

    /**
     * Returns representation of all transitions from this state.
     * This is not concrete because of a strange bug where transitions would always be null here.
     */
    public String transitionsToString() {
        return transitions.keySet().stream()
            .map(c -> transitionToString(c))
            .collect(Collectors.joining("\n"));
    }
}
