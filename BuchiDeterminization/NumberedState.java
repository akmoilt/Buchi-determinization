import java.util.*;
import java.util.stream.*;

class NumberedState extends State {
    public final Map<Character, NumberedTransition> transitions;

    public NumberedState(String id) {
        super(id, id.equals("q_0"));
        transitions = new HashMap<>();
    }

    // TODO add hashcode and equals

    @Override
    public String toString() {
        // TODO
        return "";
    }

    @Override
    public String transitionToString(Character nextChar) {
        NumberedTransition transition = transitions.get(nextChar);
        return "\t" + id + " -> " + transition.id +
            " [label=" + nextChar + "[" + transition.transitionNumber + "]\"]";
    }

    @Override
    public String transitionsToString() {
        return transitions.keySet().stream()
            .map(c -> transitionToString(c))
            .collect(Collectors.joining("\n"));
    }
}
