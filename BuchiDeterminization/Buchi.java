import java.util.*;
import java.util.stream.*;
import java.util.regex.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class represents a non-deterministic Buchi automaton (NBA).
 * It includes a method to construct an automaton from a graphviz graph.
 */
class Buchi {
    Map<String, BuchiState> states = new HashMap<>(); // This is a map because set does not have a get method

    /**
     * Interprets a graphviz graph into an automaton.
     */
    public Buchi(InputStream in) {
        Pattern statePattern = Pattern.compile("^(?<id>\\w+)\\s\\[label=\"(?<starting>\\*?)(?<label>\\w+)(?<final>\\$?)\"\\]$");
        Pattern transitionPattern = Pattern.compile("^\\s+(?<startID>\\w+)\\s->\\s(?<nextID>\\w+)\\s\\[label=(?<character>\\w)\\]$");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            for (String line : (Iterable<String>)reader.lines()::iterator) {
                Matcher stateMatcher = statePattern.matcher(line);
                if (stateMatcher.matches()) {
                    states.put(stateMatcher.group("id"), new BuchiState(stateMatcher.group("id"),
                                stateMatcher.group("starting").equals("*"),
                                stateMatcher.group("final").equals("$")));
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
                }
                else {
                    System.out.println("Invalid input format:");
                    System.out.println(line);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads NBA in graphviz format from stdin, prints the resulting DNA, again in graphviz format
     */
    public static void main(String[] args) {
        System.out.println(new Buchi(System.in)); // TODO call determinization function, move this to new class
    }

    public String toString() {
        String graphviz = states.values().stream()
            .map(BuchiState::toString)
            .collect(Collectors.joining("\n"));
        graphviz += "\n";
        graphviz += states.values().stream()
            .map(BuchiState::transitionsToString)
            .collect(Collectors.joining("\n"));
        return graphviz;
    }
}
