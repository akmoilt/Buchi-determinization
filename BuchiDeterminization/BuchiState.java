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

    @Override
    public String transitionsToString() {
        return transitions.keySet().stream()
            .map(c -> transitionToString(c))
            .collect(Collectors.joining("\n"));
    }
}
