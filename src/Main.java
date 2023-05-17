import java.util.List;

public class Main {
        public static void main(String[] args)
        {
            FileManagement fm = new FileManagement();

            if (fm.isInputValidation()) {
                List<Edge> listOfEdges = fm.getListOfEdges();
                int totalNodes = fm.getTotalNodes();
                int startingNode = fm.getStartingNode();

                // construct graph
                Graph graph = new Graph(listOfEdges, totalNodes);

                // check if the given directed graph is DAG or not
                if (graph.isDAG(totalNodes)) {
                    System.out.println("\u001B[38;5;208m" + "* * * * * * * * * * The Graph is a DAG * * * * * * * * * *" + "\u001B[0m" + "\n");
                   ShortestPathCalculator.shortestPathForDAG(graph, startingNode, totalNodes, fm.getIndexWithName());
                } else {
                    System.out.println("\u001B[38;5;208m" + "* * * * * * * * * The Graph is NOT a DAG * * * * * * * * *" + "\u001B[0m" + "\n");
                    ShortestPathCalculator.DijkstraAlgorithm(graph, startingNode, totalNodes, fm.getIndexWithName());
                }
            }
            else
                System.out.println("The Inputs Does Not Meet the Requirements");
        }
}