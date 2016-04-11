import java.util.*;
import java.util.stream.*;

/**
 * Represents a deterministic numbered automaton (DNA).
 */
class Numbered {
    Map<String, NumberedState> states;

    /**
     * Converts the given tree into a full DNA.
     */
    public Numbered(DeterminizationTree t) {
    	this.states = new LinkedHashMap<String, NumberedState>();
    	Queue<DeterminizationTree> q = new ArrayDeque<DeterminizationTree>();
    	q.add(t);
    	while(!q.isEmpty()){
            // TODO this should probably be done by id instead
    		NumberedState state = getState(t);
    		if(!this.states.containsValue(state.id)){
    			for(char s : t.getAlphabet()){
        			DeterminizationTree newT = t.doStep(s);
        			state.transitions.add(s,
        					new NumberedTransition(getState(newT).id, newT.getNumber()));
        			q.add(newT);
    			}
    		}
    		t = q.remove();
    	}
    }
    // sub-function for constructor:
    private static NumberedState getState(DeterminizationTree t){
    	String id = Arrays.toString(t.getTreeArray());
    	id += " ";
    	id += t.getStateNodeMap(); // TODO this isn't a string!
    	return new NumberedState(id);
    }

    /**
     * Prints the automaton in graphviz format.
     * TODO specify sorting
     */
    public String toString() {
        // TODO
        String graphviz = states.values().stream()
            .map(NumberedState::toString)
            .collect(Collectors.joining("\n"));
        graphviz += "\n";
        graphviz += states.values().stream()
            .map(NumberedState::transitionsToString)
            .collect(Collectors.joining("\n"));
        return graphviz;
    }
}
