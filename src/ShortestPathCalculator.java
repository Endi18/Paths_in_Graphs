import java.util.*;
import java.util.stream.IntStream;

public class ShortestPathCalculator {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void DijkstraAlgorithm(Graph graph, int source, int totalNodes, HashMap<Integer, String> indexNames) {
        List<Double> distance = new ArrayList<>(Collections.nCopies(totalNodes, Double.POSITIVE_INFINITY));
        distance.set(source, 0.0);
        int[] previous = new int[totalNodes];
        previous[source] = -1;
        boolean[] visited = new boolean[totalNodes];

        while (true) {
            int u = IntStream.range(0, totalNodes)
                    .filter(v -> !visited[v])
                    .reduce((v1, v2) -> distance.get(v1) < distance.get(v2) ? v1 : v2)
                    .orElse(-1);

            if (u == -1) {
                break; // All vertices visited
            }

            visited[u] = true;

            graph.getAdjacencyList().get(u).stream()
                    .filter(edge -> !visited[edge.getDestination()])
                    .forEach(edge -> {
                        int v = edge.getDestination();
                        double weight = edge.getWeight();

                        double newDistance = distance.get(u) + weight;
                        if (newDistance < distance.get(v)) {
                            distance.set(v, newDistance);
                            previous[v] = u;
                        }
                    });
        }

        if(indexNames == null)
            printInfoWithoutName(distance, source, previous, totalNodes);
        else
            printInfoWithName(distance, source, previous, totalNodes, indexNames);
    }

    static void longestPath(Graph graph, int source, int totalNodes, HashMap<Integer, String> indexNames) {
        List<Double> distance = new ArrayList<>(Collections.nCopies(totalNodes, Double.NEGATIVE_INFINITY));
        distance.set(source, 0.0);
        int[] previous = new int[totalNodes];
        previous[source] = -1;
        boolean[] visited = new boolean[totalNodes];

        while (true) {
            int u = IntStream.range(0, totalNodes)
                    .filter(v -> !visited[v])
                    .reduce((v1, v2) -> distance.get(v1) > distance.get(v2) ? v1 : v2)
                    .orElse(-1);

            if (u == -1) {
                break; // All vertices visited
            }

            visited[u] = true;

            graph.getAdjacencyList().get(u).stream()
                    .filter(edge -> !visited[edge.getDestination()])
                    .forEach(edge -> {
                        int v = edge.getDestination();
                        double weight = edge.getWeight();

                        double newDistance = distance.get(u) + weight;
                        if (newDistance > distance.get(v)) {
                            distance.set(v, newDistance);
                            previous[v] = u;
                        }
                    });
        }

        if(indexNames == null)
            printInfoWithoutName(distance, source, previous, totalNodes);
        else
            printInfoWithName(distance, source, previous, totalNodes, indexNames);
    }

    private static void topologicalSorting(Graph graph, int nodeNum, boolean[] visited, Stack<Integer> ordering) {
        visited[nodeNum] = true;
        for (Edge edge : graph.adjacencyList.get(nodeNum)) {
            if (!visited[edge.getDestination()]) {
                topologicalSorting(graph, edge.getDestination(), visited, ordering);
            }
        }
        ordering.push(nodeNum);
    }

    static void shortestPathForDAG(Graph graph, int source, int totalNodes, HashMap<Integer, String> indexNames) {
        Stack<Integer> ordering = new Stack<>();
        boolean[] visited = new boolean[totalNodes];

        IntStream.range(0, totalNodes)
                .filter(i -> !visited[i])
                .forEach(i -> topologicalSorting(graph, i, visited, ordering));

        List<Double> distance = new ArrayList<>(Collections.nCopies(totalNodes, Double.POSITIVE_INFINITY));
        distance.set(source, 0.0);
        int[] previous = new int[totalNodes];
        previous[source] = -1;

        while (!ordering.isEmpty()) {
            int node = ordering.pop();

            if (distance.get(node) != Double.POSITIVE_INFINITY) {
                graph.adjacencyList.get(node).forEach(edge -> {
                    int destinationNode = edge.getDestination();
                    double weight = edge.getWeight();
                    if ((distance.get(node) + weight) < distance.get(destinationNode)) {
                        distance.set(destinationNode, distance.get(node) + weight);
                        previous[destinationNode] = node;
                    }
                });
            }
        }

        if (indexNames == null)
            printInfoWithoutName(distance, source, previous, totalNodes);
        else
            printInfoWithName(distance, source, previous, totalNodes, indexNames);
    }

    private static void printInfoWithoutName(List<Double> distance, int source, int[] previous, int allNodes) {
        System.out.format(ANSI_GREEN + "%15s%15s%15s%15s", "Source", "Destination", "Min Cost", "Route\n" + ANSI_RESET);

        IntStream.range(0, allNodes)
                .forEach(i -> {
                    System.out.format("%15s%15s%15s", source, i, distance.get(i));
                    System.out.print("\t\t");
                    if(distance.get(i) == Double.POSITIVE_INFINITY)
                        System.out.println("-");
                    else {
                        printRouteWithoutNames(previous, i);
                        System.out.println();
                    }

                });
    }

    private static void printInfoWithName(List<Double> distance, int source, int[] previous, int allNodes, HashMap<Integer, String> indexNames) {
        System.out.format(ANSI_GREEN + "%15s%15s%15s%17s", "Source", "Destination", "Min Cost", "Route\n" + ANSI_RESET);

        IntStream.range(0, allNodes)
                .forEach(i -> {
                    System.out.format("%15s%15s%15s", indexNames.get(source), indexNames.get(i), distance.get(i));
                    System.out.print("\t\t");
                    if(distance.get(i) == Double.POSITIVE_INFINITY)
                        System.out.println("-");
                    else {
                        printRouteWithNames(previous, i, indexNames);
                        System.out.println();
                    }
                });
    }

    private static void printRouteWithoutNames(int[] previous, int nodeIndex) {
        if (nodeIndex >= 0) {
            printRouteWithoutNames(previous, previous[nodeIndex]);
            System.out.print(nodeIndex + " ");
        }
    }

    private static void printRouteWithNames(int[] previous, int nodeIndex, HashMap<Integer, String> indexNames) {
        if (nodeIndex >= 0) {
            printRouteWithNames(previous, previous[nodeIndex], indexNames);
            System.out.print(indexNames.get(nodeIndex) + " ");
        }
    }
}
