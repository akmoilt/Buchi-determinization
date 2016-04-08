import java.util.*;

class BuchiState extends State {
    final boolean isFinal;
    Map<Character, Set<String>> transitions = new HashMap<>(); // Maps to ids of next states

    public BuchiState(String id, boolean isInitial, boolean isFinal) {
        super(id, isInitial);
        this.isFinal = isFinal;
    }

    // TODO add hashcode and equals
}
