import java.util.*;
import java.util.stream.*;

class NumberedState extends State {
    public final Map<Character, NumberedTransition> transitions;
    String name = "We messed up!";

    public NumberedState(String id, boolean isInitial) {
        super(id, isInitial);
        transitions = new HashMap<>();
    }

    @Override
    public String toString() {
        return name + " [label=\"" + id + "\"]";
    }

    @Override
    public String transitionToString(Character nextChar) {
        NumberedTransition transition = transitions.get(nextChar);
        return "[label=\"" + nextChar + "[" + transition.transitionNumber + "]\"]";
    }
}
