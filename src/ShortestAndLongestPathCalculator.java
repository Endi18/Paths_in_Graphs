import java.util.*;
import java.util.stream.IntStream;

public class ShortestAndLongestPathCalculator {
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
                        if (distance.get(u) + edge.getWeight() < distance.get(edge.getDestination())) {
                            distance.set(edge.getDestination(), distance.get(u) + edge.getWeight());
                            previous[edge.getDestination()] = u;
                        }
                    });
        }

        printInformation(distance, source, previous, totalNodes, indexNames);
    }

    public static void longestPathForNonDAG(Graph graph, int source, int totalNodes, HashMap<Integer, String> indexNames){
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
                        if (distance.get(u) + edge.getWeight() > distance.get(edge.getDestination())) {
                            distance.set(edge.getDestination(), distance.get(u) + edge.getWeight());
                            previous[edge.getDestination()] = u;
                        }
                    });
        }

        printInformation(distance, source, previous, totalNodes, indexNames);
    }

    static void longestPathForDAG(Graph graph, int source, int totalNodes, HashMap<Integer, String> indexNames) {
        List<Double> distance = new ArrayList<>(Collections.nCopies(totalNodes, Double.NEGATIVE_INFINITY));
        distance.set(source, 0.0);

        int[] previous = new int[totalNodes];
        previous[source] = -1;

        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[totalNodes];

        IntStream.range(0, totalNodes)
                .filter(i -> !visited[i])
                .forEach(i -> topologicalSorting(graph, i, visited, stack));


        while (!stack.isEmpty()) {
            int currentNode = stack.pop();

            if (distance.get(currentNode) != Double.NEGATIVE_INFINITY) {
                graph.adjacencyList.get(currentNode)
                        .forEach(edge -> {
                            int destination = edge.getDestination();
                            if (distance.get(currentNode) + edge.getWeight() > distance.get(destination)) {
                                distance.set(destination, distance.get(currentNode) + edge.getWeight());
                                previous[destination] = currentNode;
                            }
                        });
            }
        }

        printInformation(distance, source, previous, totalNodes, indexNames);
    }

    private static void topologicalSorting(Graph graph, int nodeNum, boolean[] visited, Stack<Integer> ordering) {
        visited[nodeNum] = true;

        graph.adjacencyList.get(nodeNum).stream()
                .filter(edge -> !visited[edge.getDestination()])
                .forEach(edge -> topologicalSorting(graph, edge.getDestination(), visited, ordering));

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
                    if ((distance.get(node) + edge.getWeight()) < distance.get(edge.getDestination())) {
                        distance.set(edge.getDestination(), distance.get(node) + edge.getWeight());
                        previous[edge.getDestination()] = node;
                    }
                });
            }
        }

        printInformation(distance, source, previous, totalNodes, indexNames);
    }

    private static void printInformation(List<Double> distance, int source, int[] previous, int totalNodes, HashMap<Integer, String> indexNames){
        if (indexNames == null)
            printInformationWithoutName(distance, source, previous, totalNodes);
        else
            printInformationWithName(distance, source, previous, totalNodes, indexNames);
    }

    private static void printInformationWithoutName(List<Double> distance, int source, int[] previous, int allNodes) {
        System.out.format(ANSI_GREEN + "%15s%15s%15s%15s", "Source", "Destination", "Cost", "Route\n" + ANSI_RESET);

        IntStream.range(0, allNodes)
                .forEach(i -> {
                    System.out.format("%15s%15s%15s", source, i, distance.get(i));
                    System.out.print("\t\t");
                    if (distance.get(i) == Double.POSITIVE_INFINITY || distance.get(i) == Double.NEGATIVE_INFINITY)
                        System.out.println("-");
                    else {
                        printRouteWithoutNames(previous, i);
                        System.out.println();
                    }
                });
    }

    private static void printInformationWithName(List<Double> distance, int source, int[] previous, int allNodes, HashMap<Integer, String> indexNames) {
        System.out.format(ANSI_GREEN + "%15s%15s%15s%17s", "Source", "Destination", "Cost", "Route\n" + ANSI_RESET);

        IntStream.range(0, allNodes)
                .forEach(i -> {
                    System.out.format("%15s%15s%15s", indexNames.get(source), indexNames.get(i), distance.get(i));
                    System.out.print("\t\t");
                    if (distance.get(i) == Double.POSITIVE_INFINITY || distance.get(i) == Double.NEGATIVE_INFINITY)
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
