import java.util.*;
import java.util.stream.*;

/**
 * Represents a graph vertex + edges, and may also contain a graph.
 */
class Vertex
{
    final Set<Edge> edges;
    final Condenser contraction;
    final Optional<Condenser> parent;

    public Vertex(Set<Edge> edges, Condenser contraction, Optional<Condenser> parent) {
        this.edges = edges;
        this.contraction = contraction;
        this.parent = parent;
    }

    public Vertex() {
        this.edges = new HashSet<>();
        this.contraction = new Condenser();
        this.parent = Optional.empty();
    }

    /**
     * Adds a new outgoing edge.
     */
    public Edge addEdge(String id, int number) {
        Edge newEdge = new Edge(id, number, this);
        edges.add(newEdge);
        return newEdge;
    }

    /**
     * Returns set of all edges, including those contained in the contraction
     */
    public Set<Edge> getAllEdges() {
        // Make copy of outgoing edges
        Set<Edge> allEdges = new HashSet<>(edges);
        allEdges.addAll(contraction.getEdges());
        return allEdges;
    }

    /**
     * Checks if the MSCC containing this vertex is not trivial
     */
    public boolean inCycle() {
        return parent.isPresent();
    }
}
