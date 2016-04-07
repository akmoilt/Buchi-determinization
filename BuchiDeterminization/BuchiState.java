import java.util.*;

class BuchiState extends State {
    public final boolean isFinal;
    public final Map<Character, State> transitions;

    public BuchiState(String id, boolean isInitial, boolean isFinal) {
        super(id, isInitial);
        this.isFinal = isFinal;
        transitions = new HashMap<>();
    }

    // TODO add hashcode and equals
}
