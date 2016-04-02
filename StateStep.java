/**
 * A tuple of current state and next symbol in word.
 * Used to represent transition functions.
 */
public class Transition {
    public final State state;
    public final Symbol nextSym;

    public Transition(State state, Symbol nextSym) {
        this.state = state;
        this.nextSym = nextSym;
    }
}
