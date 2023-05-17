import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Graph {
    List<List<Edge>> adjacencyList;

    public Graph(List<Edge> listOfEdges, int number) {
        adjacencyList = IntStream.range(0, number)
                .mapToObj(i -> new ArrayList<Edge>())
                .collect(Collectors.toCollection(() -> new ArrayList<>(number)));

        Objects.requireNonNull(listOfEdges).forEach(edge -> adjacencyList.get(edge.getSource()).add(edge));
    }

    private boolean isCyclic(int nodeNumber, boolean[] visited, boolean[] recursionStack) {
        visited[nodeNumber] = true;
        recursionStack[nodeNumber] = true;

        return adjacencyList.get(nodeNumber).stream()
                .map(Edge::getDestination)
                .anyMatch(neighbor -> {
                    if (!visited[neighbor]) {
                        return isCyclic(neighbor, visited, recursionStack);
                    } else {
                        return recursionStack[neighbor];
                    }
                });
    }

    public boolean isDAG(int totalNodes) {
        boolean[] visited = new boolean[totalNodes];
        boolean[] recursionStack = new boolean[totalNodes];

        return IntStream.range(0, totalNodes)
                .filter(i -> !visited[i] && isCyclic(i, visited, recursionStack))
                .findFirst()
                .isEmpty();
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}
