import java.util.*;

/**
 * Represents a graph vertex + edges, and may also contain a graph.
 */
class Vertex
{
    final Set<Edge> edges;
    final Condenser contraction;

    public Vertex(Set<Edge> edges, Condenser contraction) {
        this.edges = edges;
        this.contraction = contraction;
    }
}
