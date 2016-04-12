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
    		DeterminizationTree curr = q.remove();
    		NumberedState state = getState(curr);
    		if(!this.states.containsKey(state.id)){
    			for(char s : curr.getAlphabet()){
        			DeterminizationTree newT = curr.doStep(s);
        			state.transitions.put(s,
        					new NumberedTransition(getState(newT).id, newT.getNumber()));
        			q.add(newT);
    			}
                states.put(state.id, state);
    		}
    	}
    }
    // sub-function for constructor:
    private static NumberedState getState(DeterminizationTree t){
    	String id = Arrays.toString(t.getTreeArray());
    	id += " ";
    	id += t.getStateNodeMap();
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
