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

    public String toString() {
        return from + " --" + number + "-> " + to;
    }
}
