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
