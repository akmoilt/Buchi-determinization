import java.util.*;
import java.util.stream.*;

/**
 * Represents a deterministic numbered automaton (DNA).
 */
class Numbered {
    Map<String, NumberedState> states;
    List<String> sortedIDs;

    /**
     * Converts the given tree into a full DNA.
     */
    public Numbered(DeterminizationTree t) {
    	this.states = new LinkedHashMap<String, NumberedState>();
    	Queue<DeterminizationTree> q = new ArrayDeque<DeterminizationTree>();
    	q.add(t);
        boolean isInitial = true;
    	while(!q.isEmpty()){
            // TODO this should probably be done by id instead
    		DeterminizationTree curr = q.remove();
    		NumberedState state = new NumberedState(getStateID(curr), isInitial);
    		if(!this.states.containsKey(state.id)){
    			for(char s : curr.getAlphabet()){
        			DeterminizationTree newT = curr.doStep(s, true);
        			state.transitions.put(s,
        					new NumberedTransition(getStateID(newT), newT.getNumber()));
        			q.add(newT);
    			}
                states.put(state.id, state);
    		}
            isInitial = false;
    	}

        sortedIDs = states.keySet().stream().sorted().collect(Collectors.toList());
        int index = 0;
        int initialIndex = 0;
        NumberedState initialState = null;
        for (String id : sortedIDs) {
            NumberedState state = states.get(id);
            if (state.isInitial) {
                state.name = "Q0";
                initialIndex = index;
                initialState = state;
            }
            else {
                index++;
                state.name = "Q" + index;
            }
        }
        sortedIDs.remove(initialIndex);
        sortedIDs.add(0, initialState.id);
    }
    // sub-function for constructor:
    private static String getStateID(DeterminizationTree t){
    	String id = Arrays.toString(t.getTreeArray()).replace(",", "");
    	id += ",";
    	id += t.getStateMappingString();
        return id;
    }

    /**
     * Prints the automaton in graphviz format.
     */
    public String toString() {
        String graphviz = sortedIDs.stream()
            .map(id -> "\t" + states.get(id).toString())
            .collect(Collectors.joining("\n"));
        graphviz += "\n";
        graphviz += sortedIDs.stream()
            .map(id -> states.get(id).transitions.entrySet().stream()
                    .map(entry -> "\t\t" + states.get(id).name + "->" +
                        states.get(entry.getValue().id).name + " " +
                        states.get(id).transitionToString(entry.getKey()))
                    .sorted().collect(Collectors.joining("\n")))
            .collect(Collectors.joining("\n"));
        return graphviz;
    }
}
