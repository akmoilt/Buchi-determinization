import java.util.*;

class BuchiState extends State {
    final boolean isFinal;
    Map<Character, State> transitions = new HashMap<>(); // TODO changed this to map to id instead of the whole state

    public BuchiState(String id, boolean isInitial, boolean isFinal) {
        super(id, isInitial);
        this.isFinal = isFinal;
    }

    // TODO add hashcode and equals
}
