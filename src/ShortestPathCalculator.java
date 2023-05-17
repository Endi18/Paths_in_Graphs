import java.util.*;
import java.util.stream.IntStream;

public class ShortestPathCalculator {
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

//    private static int topologicalSorting(Graph graph, int nodeNum, boolean[] visited, int[] departure, int time, Stack<Integer> ordering) {
//        // Set the current node as visited
//        visited[nodeNum] = true;
//        // for loop to iterate through the edges for each node
//        for (Edge edge : graph.adjacencyList.get(nodeNum)) {
//            // if `u` is not yet visited
//            if (!visited[edge.getDestination()]) {
//                time = topologicalSorting(graph, edge.getDestination(), visited, departure, time, ordering);
//            }
//        }
//        // Push the element to the stack to be used by topological sort since stack inserts are O(1)
//        ordering.push(nodeNum);
//        // Set the departure time for current node
//        departure[nodeNum] = time++;
//        // Return the time count
//        return time;
//    }
//
//    static void shortestPathForDAG(Graph graph, int source, int totalNodes, HashMap<Integer, String> indexNames) {
//        // Creating stack to store ordering from DFS
//        Stack<Integer> ordering = new Stack<>();
//        // Constant variable to save time count
//        int time = 0;
//        // Boolean array to store visited nodes
//        boolean[] visited = new boolean[totalNodes];
//        // Integer array to store departure time for the nodes
//        int[] departure = new int[totalNodes];
//        // for loop to perform DFS traversal
//        for (int i = 0; i < totalNodes; i++) {
//            if (!visited[i]) {
//                time = topologicalSorting(graph, i, visited, departure, time, ordering);
//            }
//        }
//        // List to save the distances, initially setting all distances to infinity and source distance to itself equal to zero
//        List<Double> distance = new ArrayList<>(Collections.nCopies(totalNodes, Double.POSITIVE_INFINITY));
//        distance.set(source, 0.0);
//        // Array to save predecessors
//        int[] previous = new int[totalNodes];
//        previous[source] = -1;
//        // while loop to iterate through the ordering
//        while (!ordering.isEmpty()) {
//            // Getting vertex from ordering
//            int u = ordering.pop();
//
//            // if condition to check if distance is not infinity
//            if (distance.get(u) != Double.POSITIVE_INFINITY) {
//                graph.adjacencyList.get(u).forEach(edge -> {
//                    int v = edge.getDestination();
//                    double weight = edge.getWeight();
//                    if ((distance.get(u) + weight) < distance.get(v)) {
//                        distance.set(v, distance.get(u) + weight);
//                        previous[v] = u;
//                    }
//                });
//            }
//        }
//        // Print the required info
//        if(indexNames == null)
//            printInfoWithoutName(distance, source, previous, totalNodes);
//        else
//            printInfoWithName(distance, source, previous, totalNodes, indexNames);
//    }

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
        System.out.format("\u001B[32m" + "%15s%15s%15s%15s", "Source", "Destination", "Min Cost", "Route\n" + "\u001B[0m");

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
        System.out.format("\u001B[32m" + "%15s%15s%15s%17s", "Source", "Destination", "Min Cost", "Route\n" + "\u001B[0m");

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
