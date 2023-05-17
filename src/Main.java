import java.util.List;

public class Main {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_ORANGE = "\u001B[38;5;208m";

    public static void main(String[] args) {

        FileManagement fm = new FileManagement();

        if (fm.isInputValidation()) {
            List<Edge> listOfEdges = fm.getListOfEdges();
            int totalNodes = fm.getTotalNodes();
            int startingNode = fm.getStartingNode();

            // construct graph
            Graph graph = new Graph(listOfEdges, totalNodes);

            // check if the given directed graph is DAG or not
            if (graph.isDAG(totalNodes)) {
                System.out.println(ANSI_ORANGE + "* * * * * * * * * * The Graph is a DAG * * * * * * * * * *" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Shortest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.shortestPathForDAG(graph, startingNode, totalNodes, fm.getIndexWithName());
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Longest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.longestPathForDAG(graph, startingNode, totalNodes, fm.getIndexWithName());
            } else {
                System.out.println(ANSI_ORANGE + "* * * * * * * * * The Graph is NOT a DAG * * * * * * * * *" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Shortest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.DijkstraAlgorithm(graph, startingNode, totalNodes, fm.getIndexWithName());
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Longest Path---!>" + ANSI_RESET);
            }
        } else
            System.out.println("The Inputs Does Not Meet the Requirements");
    }
}