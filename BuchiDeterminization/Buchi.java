import java.util.*;
import java.util.regex.*;

/**
 * This class represents a non-deterministic Buchi automaton (NBA).
 * It includes a method to construct an automaton from a graphviz graph.
 */
class Buchi {
    Map<String, BuchiState> states = new HashMap<>(); // This is a map because set does not have a get method

    /**
     * Interprets a graphviz graph into an automaton.
     */
    public Buchi(String graphviz) {
        Pattern statePattern = Pattern.compile("^(?<id>\\w+)\\s\\[label=\"(?<starting>\\*?)(?<labelid>)(?<final>\\$?)\"\\]$");
        Pattern transitionPattern = Pattern.compile("^\t(?<startID>\\w+)\\s->\\s(?<nextID>\\w+)\\s\\[label=(?<character>\\w)\\]$");
        
        Scanner scanner  = new Scanner(graphviz);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher stateMatcher = statePattern.matcher(line);
            if (stateMatcher.matches()) {
                states.put(stateMatcher.group("id"), new BuchiState(stateMatcher.group("id"), stateMatcher.group("starting") == "*",
                        stateMatcher.group("final") == "$"));
                continue;
            }
            Matcher transitionMatcher = transitionPattern.matcher(line);
            if (transitionMatcher.matches()) {
                BuchiState state = states.get(transitionMatcher.group("startID"));
                Character nextChar = transitionMatcher.group("character").charAt(0);
                if (state.transitions.containsKey(nextChar)) {
                    state.transitions.get(nextChar).add(transitionMatcher.group("nextID"));
                }
                else {
                    // convert a single element to an array as ugly hack to initialize set with value
                    state.transitions.put(nextChar, new HashSet<String>(Arrays.asList(transitionMatcher.group("nextID"))));
                }
                continue;
            }
            else {
                System.out.println("Invalid input format.");
            }
        }
        scanner.close();
    }
}
