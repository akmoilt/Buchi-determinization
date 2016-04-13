import java.util.*;
import java.util.stream.*;

abstract class State {
    public final String id;
    public final boolean isInitial;
    Map<Character, ?> transitions;

    public State(String id, boolean isInitial) {
        this.id = id;
        this.isInitial = isInitial;
    }

    /**
     * States are equal if they have the same id.
     * ids should be unique
     */
    public boolean equals(State other) {
        return this.id.equals(other.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * Returns string representing state in graphviz format
     */
    public abstract String toString();

    /**
     * Returns string representing the set of transitions in graphviz format
     */
    public abstract String transitionToString(Character nextChar);
}
