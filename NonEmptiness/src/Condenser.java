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
     * Returns a new condenser with all edges with a number less than cutoff removed.
     */
    public Condenser cutoff(int cutoff) {
        Condenser res = new Condenser();
        Set<String> vertsToKeep = new HashSet<>();

        for (Map.Entry<String, Vertex> vertEntry : vertices.entrySet()) {
            Vertex vertex = vertEntry.getValue();
            String id = vertEntry.getKey();
            Set<Edge> edges = new HashSet<>();
            for (Edge edge : vertex.edges) {
                // Filter out edges
                if (edge.number >= cutoff) {
                    edges.add(edge);
                    // We also want to keep any vertices with incoming edges
                    vertsToKeep.add(edge.id);
                }
            }
            res.vertices.put(id, new Vertex(edges, vertex.contraction.cutoff(cutoff)));
            
            // We want to keep this vertex if it has outgoing edges
            if (!edges.isEmpty()) {
                vertsToKeep.add(id);
            }
        }

        // Only keep vertices that are connected (those in vertsToKeep)
        res.vertices.keySet().retainAll(vertsToKeep);

        return res;
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
