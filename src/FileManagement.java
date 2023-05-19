import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileManagement {
    private final List<Edge> listOfEdges;
    private int totalNodes;

    private HashMap<Integer, String> indexWithName = new HashMap<>();
    private boolean inputValidation;
    private int startingNode;

    public HashMap<Integer, String> getIndexWithName() {
        return indexWithName;
    }

    public boolean isInputValidation() {
        return inputValidation;
    }

    public void setInputValidation(boolean inputValidation) {
        this.inputValidation = inputValidation;
    }

    public int getStartingNode() {
        return startingNode;
    }

    public void setStartingNode(int startingNode) {
        this.startingNode = startingNode;
    }

    FileManagement(int number){
        listOfEdges = new ArrayList<>();
        readFile(number);
    }

    public List<Edge> getListOfEdges() {
        return listOfEdges;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public void readFile(int number) {
        try (Stream<String> lines = Files.lines(Paths.get(".", "src", "graph.txt"))) {
            AtomicInteger lineCount = new AtomicInteger(1);

            lines.forEach(line -> {
                String[] parts = line.split(" ");
                switch (lineCount.getAndIncrement()) {
                    case 1 -> {
                        if (validateInputs(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]))) {
                            setInputValidation(true);
                            setTotalNodes(Integer.parseInt(parts[0]));
                        } else {
                            setInputValidation(false);
                        }
                    }
                    case 2 -> {
                        if (parts.length > 1) {
                            IntStream.range(0, getTotalNodes()).forEach(i -> indexWithName.put(i, parts[i]));
                        } else if (parts[0].equals("use-indexes")) {
                            indexWithName = null;
                        }
                    }
                    case 3 -> setStartingNode(Integer.parseInt(parts[0]));
                    default -> {
                        if (number == 0) {
                            listOfEdges.add(new Edge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2])));
                        } else if (number == 1) {
                            Edge edge = new Edge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), (Double.parseDouble(parts[2]) * -1.00));
                            listOfEdges.add(edge);
                            System.out.println("EDGE WEIGHT " + edge.getWeight());
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateInputs(int numberOfNodes, int numberOfEdges){
        return numberOfNodes >= 1 && numberOfNodes <= 100000 && numberOfEdges >= 1 && numberOfEdges <= 10000;
    }
}
