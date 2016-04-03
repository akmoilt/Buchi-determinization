import java.util.*;

class NumberedState extends State {
    private Map<Character, NumberedTransition> transitions;

    public NumberedState(String id, boolean isInitial) {
        super(id, isInitial);
        transitions = new HashMap<>();
    }

    // TODO add hashcode and equals
}
