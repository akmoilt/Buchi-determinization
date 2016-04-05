abstract class State {
    public final String id;
    public final boolean isInitial;

    public State(String id, boolean isInitial) {
        this.id = id;
        this.isInitial = isInitial;
    }
}
