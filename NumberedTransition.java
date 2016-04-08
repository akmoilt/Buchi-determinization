public class NumberedTransition {
    public final State state;
    public final int transitionNumber;

    public NumberedTransition(State state, int transitionNumber) {
        this.state = state;
        this.transitionNumber = transitionNumber;
    }
}
