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
            Vertex vertex = new Vertex(state.id, new HashSet<>(), new Condenser());
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
        Condenser gMid = g.filterEdges(e -> e.number >= mid);
        Components components = gMid.getComponents();
        Condenser gMidCondensation = gMid.contract(components);
        if (mid % 2 != 0) {
            if (gMidCondensation.vertices.values().stream()
                    .flatMap(v -> v.contraction.vertices.values().stream())
                    .flatMap(v -> v.edges.stream())
                    .anyMatch(e -> e.number == mid)) {
                return true;
            }
        }

        // We search for a witness in {mid+1, ..., endNumber}
        if (solve(gMidCondensation.stepDown().filterEdges(e -> e.number >= mid+1), mid+1, endNumber)) {
            return true;
        }
        return solve(gMidCondensation.stepUp(mid, g, components.roots), startNumber, mid-1);
    }
}
