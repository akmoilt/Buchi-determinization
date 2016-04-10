abstract class State {
    public final String id;
    public final boolean isInitial;

    public State(String id, boolean isInitial) {
        this.id = id;
        this.isInitial = isInitial;
    }

    /**
     * Returns string representing state in graphviz format
     */
    public abstract String toString();

    /**
     * Returns string representing the set of transitions in graphviz format
     */
    public abstract String transitionToString(Character nextChar);

    /**
     * Returns representation of all transitions from this state
     */
    public abstract String transitionsToString();
}
