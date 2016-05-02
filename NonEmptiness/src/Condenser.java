import java.util.*;
import java.util.stream.*;

/**
 * Represents a graph in which each node may also contain a graph.
 * This allows us to comfortably represent contractions and condensations.
 */
class Condenser
{
    final Map<String, Vertex> vertices = new HashMap<>();

    public Vertex getVert(String id) {
        return vertices.get(id);
    }

    public Vertex addVert(Vertex vertex) {
        vertices.put(vertex.id, vertex);
        return vertex;
    }

    /**
     * Contracts every maximal strongly-connected component into a single vertex.
     * Uses Kosaraju's algorithm.
     */
    public Condenser condensation() {
        Deque<Vertex> stack = new ArrayDeque<>();
        Condenser transpose = this.transpose();
        Map<String, String> roots = new HashMap<>();
        Map<String, Set<Vertex>> components = new HashMap<>();
        for (Vertex vertex : vertices.values()) {
            visit(vertex, stack, roots);
        }

        for (Vertex vertex : stack) {
            assign(vertex, vertex, roots, components, transpose);
        }

        return contract(components, roots);
    }

    /**
     * Helper function for condensation, does dfs and adds to stack in post-order.
     */
    private void visit(Vertex vertex, Deque<Vertex> stack, Map<String, String> roots) {
        if (!roots.containsKey(vertex.id)) {
            roots.put(vertex.id, null);
            for (Edge edge : vertex.edges) {
                visit(getVert(edge.to), stack, roots);
            }
            stack.push(vertex);
        }
    }

    /**
     * Helper function for condensation, does dfs over transpose and creates the components.
     */
    private void assign(Vertex vertex, Vertex root, Map<String, String> roots, Map<String, Set<Vertex>> components, Condenser transpose) {
        if (roots.get(vertex.id) == null) {
            roots.put(vertex.id, root.id);
            if (!components.containsKey(root.id)) {
                components.put(root.id, new HashSet<>());
            }
            components.get(root.id).add(vertex);
            for (Edge inEdge : transpose.getVert(vertex.id).edges) {
                assign(getVert(inEdge.to), root, roots, components, transpose);
            }
        }
    }

    /**
     * Returns a transposition of this graph (every edge is reversed).
     */
    public Condenser transpose() {
        Condenser transpose = new Condenser();
        vertices.keySet().stream().forEach(id -> transpose.addVert(new Vertex(id)));
        for (Vertex vertex : vertices.values()) {
            for (Edge edge : vertex.edges) {
                transpose.getVert(edge.to).addEdge(edge.from, edge.number);
            }
        }
        return transpose;
    }

    /**
     * Returns a new graph where every set of vertices in components is contracted to a single vertex.
     * TODO union-find
     */
    public Condenser contract(Map<String, Set<Vertex>> components, Map<String, String> roots) {
        Condenser contraction = new Condenser();

        for (Set<Vertex> component : components.values()) {
            Vertex root = getVert(roots.get(component.iterator().next().id));
            Vertex newRoot = new Vertex(root.id,
                    root.edges.stream().map(e -> new Edge(root.id, roots.get(e.to), e.number)).collect(Collectors.toSet()),
                    new Condenser(), Optional.empty());
            contraction.addVert(newRoot);
            if (component.size() != 1) {
                for (Vertex vertex : component) {
                    Vertex newVert = new Vertex(root.id + "." + vertex.id, new HashSet<>(), new Condenser(), Optional.of(contraction));
                    for (Edge edge : vertex.edges) {
                        if (getVert(edge.to).equals(root.id)) {
                            // Edge inside the component
                            newVert.addEdge(root.id + "." + edge.to, edge.number);
                        }
                        else if (vertex != root){
                            // Edge goes to vertex outside component
                            root.addEdge(roots.get(edge.to), edge.number);
                        }
                    }
                    newRoot.contraction.addVert(newVert);
                }
            }
        }

        return contraction;
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
            Vertex newVert = new Vertex(id, new HashSet<>(), vertex.contraction.cutoff(cutoff), Optional.of(res));
            for (Edge edge : vertex.edges) {
                // Filter out edges
                if (edge.number >= cutoff) {
                    newVert.addEdge(edge.to, edge.number);
                    // We also want to keep any vertices with incoming edges
                    vertsToKeep.add(edge.to);
                }
            }
            res.addVert(newVert);

            // We want to keep this vertex if it has outgoing edges
            if (!newVert.edges.isEmpty()) {
                vertsToKeep.add(id);
            }
        }

        // Only keep vertices that are connected (those in vertsToKeep)
        res.vertices.keySet().retainAll(vertsToKeep);

        return res;
    }

    public String toString() {
        return vertices.toString();
    }

    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    /**
     * Returns the maximum number of an edge in the graph.
     * Does not recurse into contractions
     */
    public int getMaxNum() {
        Optional<Integer> max = vertices.values().stream().flatMap(v -> v.edges.stream().map(e -> e.number))
            .max(Comparator.naturalOrder());
        if (!max.isPresent()) {
            return 0;
        }
        return max.get();
    }
}
