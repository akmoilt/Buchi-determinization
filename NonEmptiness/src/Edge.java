class Edge
{
    final String from;
    final String to;
    final int number;

    public Edge(String from, String to, int number) {
        this.from = from;
        this.to = to;
        this.number = number;
    }

    /**
     * Checks if this edge belongs to a cycle.
     */
    public boolean inCycle() {
        // TODO
        return true;
    }

    public String toString() {
        return from + " --" + number + "-> " + to;
    }
}
