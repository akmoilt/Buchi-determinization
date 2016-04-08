import java.util.*;

class NumberedState extends State {
    private Map<Character, NumberedTransition> transitions;

    public NumberedState(String id) {
        super(id, id.equals("q_0"));
        transitions = new HashMap<>();
    }

    // TODO add hashcode and equals
}
