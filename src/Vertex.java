public class Vertex implements Comparable<Vertex> {
    int vertexIndex;
    double distance;

    public Vertex(int vertexIndex, double distance) {
        this.vertexIndex = vertexIndex;
        this.distance = distance;
    }

    public int compareTo(Vertex other) {
        return Double.compare(this.distance, other.distance);
    }
}
