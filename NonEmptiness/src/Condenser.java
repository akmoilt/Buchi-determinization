import java.util.*;
import java.util.stream.*;

/**
 * Represents a graph in which each node may also contain a graph.
 * This allows us to comfortably represent contractions and condensations.
 */
class Condenser
{
    final Map<String, Vertex> vertices = new HashMap<>();

    /**
     * Contracts every maximal strongly-connected component into a single vertex.
     */
    public Condenser condensation() {
        // TODO
        return this;
    }

    /**
     * Removes all top-level vertices and nodes, brings up their contracted graphs.
     */
    public Condenser stepDown() {
        Condenser ret = new Condenser();
        for (Vertex vertex : vertices.values()) {
            ret.vertices.putAll(vertex.contraction.vertices);
        }
        return ret;
    }

    /**
     * Removes all contractions from vertices -- keeps only the top level.
     */
    public Condenser stepUp() {
        Condenser ret = new Condenser();
        for (Vertex vertex : vertices.values()) {
            vertex.contraction.vertices.clear();
        }
        return ret;
    }

    /**
     * Returns a new condenser with all edges with a number less than cutoff removed.
     */
    public Condenser cutoff(int cutoff) {
        Condenser res = new Condenser();
        Set<String> vertsToKeep = new HashSet<>();

        for (Map.Entry<String, Vertex> vertEntry : vertices.entrySet()) {
            Vertex vertex = vertEntry.getValue();
            String id = vertEntry.getKey();
            Vertex newVert = new Vertex(new HashSet<>(), vertex.contraction.cutoff(cutoff), Optional.of(res));
            for (Edge edge : vertex.edges) {
                // Filter out edges
                if (edge.number >= cutoff) {
                    newVert.addEdge(edge.id, edge.number);
                    // We also want to keep any vertices with incoming edges
                    vertsToKeep.add(edge.id);
                }
            }
            res.vertices.put(id, newVert);
            
            // We want to keep this vertex if it has outgoing edges
            if (!newVert.edges.isEmpty()) {
                vertsToKeep.add(id);
            }
        }

        // Only keep vertices that are connected (those in vertsToKeep)
        res.vertices.keySet().retainAll(vertsToKeep);

        return res;
    }

    /**
     * Returns a set of all edges in the graph (including contractions)
     */
    public Set<Edge> getEdges() {
        return vertices.values().stream().flatMap(e -> e.getAllEdges().stream()).collect(Collectors.toSet());
    }

    /**
     * Returns the maximum number of an edge in the graph, or that number + 1, whichever is even.
     * Does not recurse into contractions
     */
    public int get2k() {
        Optional<Integer> max = vertices.values().stream().flatMap(v -> v.edges.stream().map(e -> e.number))
            .max(Comparator.naturalOrder());
        if (!max.isPresent()) {
            return 0;
        }

        int maxNum = max.get();
        if (maxNum % 2 == 0) {
            return maxNum;
        }
        else {
            return maxNum + 1;
        }
    }
}
