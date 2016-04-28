class Edge
{
    final String id;
    final int number;
    final Vertex source;

    public Edge(String id, int number, Vertex source) {
        this.id = id;
        this.number = number;
        this.source = source;
    }

    /**
     * Checks if this edge belongs to a cycle.
     */
    public boolean inCycle() {
        // TODO
        return true;
    }
}
