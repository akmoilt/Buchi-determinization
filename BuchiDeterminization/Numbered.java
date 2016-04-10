import java.util.*;

/**
 * Represents a deterministic numbered automaton (DNA).
 */
class Numbered {
    private Set<NumberedState> states;

    /**
     * Converts the given tree into a full DNA.
     */
    public Numbered(DeterminizationTree t) {
    	this.states = new LinkedHashedSet<NumberedState>();
    	Queue<DeterminizationTree> q = new Queue<DeterminizationTree>();
    	q.add(t);
    	while(!q.isEmpty()){
    		NumberedState state = getState(t);
    		if(!this.states.contains(state)){
    			// TODO add transition and resulting trees to queue
    		}
    		t = q.remove();
    	}
    }
    // sub-function for constructor:
    public static NumberedState getState(DeterminizationTree t){
    	string id = Arrays.toString(t.getTreeArray());
    	id += " ";
    	id += Arrays.toString(t.getStateNodeMap().toArray());
    	return new NumberedState();
    }

    /**
     * Prints the automaton in graphviz format.
     * TODO specify sorting
     */
    public String toString() {
        // TODO
        return "";
    }
}
