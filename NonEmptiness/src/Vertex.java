import java.util.*;
import java.util.stream.*;

/**
 * Represents a graph vertex + edges, and may also contain a graph.
 */
class Vertex
{
    final String id;
    final Set<Edge> edges;
    final Condenser contraction;
    final Optional<Condenser> parent;

    public Vertex(String id, Set<Edge> edges, Condenser contraction, Optional<Condenser> parent) {
        this.id = id;
        this.edges = edges;
        this.contraction = contraction;
        this.parent = parent;
    }

    public Vertex(String id) {
        this.id = id;
        this.edges = new HashSet<>();
        this.contraction = new Condenser();
        this.parent = Optional.empty();
    }

    /**
     * Adds a new outgoing edge.
     */
    public Edge addEdge(String to, int number) {
        Edge newEdge = new Edge(this.id, to, number);
        edges.add(newEdge);
        return newEdge;
    }

    /**
     * Checks if the MSCC containing this vertex is not trivial
     */
    public boolean inCycle() {
        return parent.isPresent();
    }

    public String toString() {
        return id + ":" + edges + (contraction.isEmpty() ? "" : ";" + contraction);
    }
}
