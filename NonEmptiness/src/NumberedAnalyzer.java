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
            Set<Edge> edges = state.transitions.values().stream().map(nt -> new Edge(nt.id, nt.transitionNumber)).collect(Collectors.toSet());
            Vertex vertex = new Vertex(edges, new Condenser());
            numberedGraph.vertices.put(state.id, vertex);
        }
    }
	
    public boolean isEmpty()
    {
        return solve(numberedGraph, 1, numberedGraph.get2k());
    }

    /** Recursive function to find witnesses for the even cycle problem.
     * Taken from: http://www.cs.huji.ac.il/~ornak/publications/fossacs01.pdf
     */
    public boolean solve(Condenser g, int startNumber, int endNumber) {
        if (endNumber < startNumber) {
            return false;
        }

        int mid = (int)Math.ceil((startNumber + endNumber) / 2.0);
        // Filter out all the edges with number less than mid
        // TODO parity automata are numbered on the vertices, does this change the algorithm?
        Condenser Gmid = g.cutoff(mid);

        // TODO remove this return, it's just for compilation
        return true;
    }

    public static void main(String[] args) {
        Numbered numbered = new Numbered(System.in);
        NumberedAnalyzer analyzer = new NumberedAnalyzer(numbered);
        System.out.println(!analyzer.isEmpty());
    }
}
