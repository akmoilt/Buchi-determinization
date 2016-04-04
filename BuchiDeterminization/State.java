abstract class State {
    protected final String id;
    protected final boolean isInitial;

    public State(String id, boolean isInitial) {
        this.id = id;
        this.isInitial = isInitial;
    }
}
