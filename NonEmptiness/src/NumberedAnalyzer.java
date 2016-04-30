import java.util.*;
import java.util.stream.*;
import java.util.stream.Collectors.*;

/**
 * This class is an analyzer for numbered parity automata.
 */
public class NumberedAnalyzer {
    private Condenser numberedGraph;
	
	public NumberedAnalyzer(Numbered numbered)
    {
        numberedGraph = new Condenser();
        for (NumberedState state : numbered.states.values()) {
            Vertex vertex = new Vertex(state.id, new HashSet<>(), new Condenser(), Optional.empty());
            for (NumberedTransition transition : state.transitions.values()) {
                vertex.addEdge(transition.id, transition.transitionNumber);
            }
            numberedGraph.addVert(vertex);
        }
    }
	
    public boolean isEmpty()
    {
        return !solve(numberedGraph, 1, numberedGraph.getMaxNum());
    }

    /** Recursive function to find witnesses for the even cycle problem.
     * Returns true if there is a witness, false otherwise.
     * Taken from: http://www.cs.huji.ac.il/~ornak/publications/fossacs01.pdf
     */
    public boolean solve(Condenser g, int startNumber, int endNumber) {
        if (endNumber < startNumber) {
            return false;
        }

        int mid = (int)Math.ceil((startNumber + endNumber) / 2.0);
        // Filter out all the edges with number less than mid
        // TODO parity automata are numbered on the vertices, does this change the algorithm?
        Condenser gMid = g.cutoff(mid);
        Condenser gMidCondensation = gMid.condensation();
        if (mid % 2 == 0) {
            if (gMidCondensation.getEdges().stream().anyMatch(e -> e.number == mid && e.inCycle())) {
                // there  is an edge numbered mid in a non-trivial MSCC
                return true;
            }
        }

        // We search for a witness in {mid+1, ..., endNumber}
        if (solve(gMidCondensation.stepDown().cutoff(mid+1), mid+1, endNumber)) {
            return true;
        }
        return solve(gMidCondensation.stepUp(), startNumber, mid-1);
    }

    public static void main(String[] args) {
        Numbered numbered = new Numbered(System.in);
        NumberedAnalyzer analyzer = new NumberedAnalyzer(numbered);
        System.out.println("This automaton is nonempty: " + !analyzer.isEmpty());
    }
}
