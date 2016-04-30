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
     * Uses the path-based algorithm: https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm
     */
    public Condenser condensation() {
        Deque<Vertex> stack = new ArrayDeque<>();
        Deque<Integer> boundaries = new ArrayDeque<>();
        Map<Vertex, Integer> preorderNums = new HashMap<>();
        Set<Vertex> identified = new HashSet<>();
        Set<Set<Vertex>> components = new HashSet<>();
        
        for (Vertex vertex : vertices.values()) {
            if (!preorderNums.containsKey(vertex)) {
                components.addAll(dfs(vertex, stack, boundaries, preorderNums, identified));
            }
        }

        return this.contract(components);
    }

    /**
     * Recursive helper function to find SCCs using DFS.
     */
    private Set<Set<Vertex>> dfs(Vertex vertex, Deque<Vertex> stack, Deque<Integer> boundaries, Map<Vertex, Integer>preorderNums, Set<Vertex> identified) {
        preorderNums.put(vertex, stack.size());
        stack.push(vertex);
        boundaries.push(preorderNums.get(vertex));
        Set<Set<Vertex>> components = new HashSet<>();

        for (Edge edge : vertex.edges) {
            Vertex nextVert = getVert(edge.to);
            if (!preorderNums.containsKey(nextVert)) {
                components.addAll(dfs(nextVert, stack, boundaries, preorderNums, identified));
            }
            else if (!identified.contains(nextVert)) {
                while (preorderNums.get(nextVert) < boundaries.peek()) {
                    boundaries.pop();
                }
            }

            if (boundaries.peek() == preorderNums.get(vertex)) {
                boundaries.pop();
                Set<Vertex> scc = new HashSet<>();
                Vertex vertToAdd;
                while ((vertToAdd = stack.pop()) != vertex) {
                    scc.add(vertToAdd);
                }
                scc.add(vertToAdd);
                identified.addAll(scc);
                components.add(scc);
            }
        }

        return components;
    }

    /**
     * Returns a new graph where every set of vertices in components is contracted to a single vertex.
     * TODO union-find
     */
    public Condenser contract(Set<Set<Vertex>> components) {
        Map<String, String> roots = new HashMap<>();
        // Map each vertex to an arbitrary root of its component
        // TODO move this into dfs to do everything in one pass
        for (Set<Vertex> component : components) {
            Vertex root = component.iterator().next();
            for (Vertex vertex : component) {
                roots.put(vertex.id, root.id);
            }
        }

        Condenser contraction = new Condenser();
        for (Set<Vertex> component : components) {
            Vertex root = getVert(roots.get(component.iterator().next().id));
            Vertex newRoot = new Vertex(root.id,
                    root.edges.stream().map(e -> new Edge(root.id, roots.get(e.to), e.number)).collect(Collectors.toSet()),
                    new Condenser(), Optional.empty());
            contraction.addVert(newRoot);
            for (Vertex vertex : component) {
                Vertex newVert = new Vertex(root.id + "." + vertex.id, new HashSet<>(), new Condenser(), Optional.of(contraction));
                // We need to copy the edges so that when we reach the root and add edges to it, we won't modify the iterator
                for (Edge edge : new HashSet<>(vertex.edges)) {
                    if (getVert(edge.to).equals(root.id)) {
                        // Edge inside the component
                        newVert.addEdge(root.id + "." + edge.to, edge.number);
                    }
                    else {
                        // Edge goes to vertex outside component
                        root.addEdge(roots.get(edge.to), edge.number);
                    }
                }
                newRoot.contraction.addVert(newVert);
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

    /**
     * Returns a set of all edges in the graph (including contractions)
     */
    public Set<Edge> getEdges() {
        return vertices.values().stream().flatMap(e -> e.getAllEdges().stream()).collect(Collectors.toSet());
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
