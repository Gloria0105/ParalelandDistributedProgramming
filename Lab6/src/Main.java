import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        int GRAPHS_COUNT = 10;

        List<DirectedGraph> graphs = new ArrayList<>();

        for (int i = 1; i <= GRAPHS_COUNT; i++) {
            DirectedGraph graph = generateRandomHamiltonian(i);
            graphs.add(graph);
            System.out.println(graph);
        }

        System.out.println("Sequential");
        batchTesting(graphs, 1);

        System.out.println("Parallel");
        batchTesting(graphs, 4);

    }

    private static void batchTesting(List<DirectedGraph> graphs, int threadCount) throws InterruptedException {
        for (int i = 0; i < graphs.size(); i++) {
            long startTime = System.nanoTime();
            findHamiltonian(graphs.get(i), threadCount);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            if(i % 10 == 0)
                System.out.println("Size " + i + ": " + duration + " ms");
        }
    }

    private static void findHamiltonian(DirectedGraph graph, int threadCount) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        Lock lock = new ReentrantLock();
        List<Integer> result = new ArrayList<>(graph.size());

        for (int i = 0; i < graph.size(); i++){
            pool.execute(new Task(graph, i, result, lock));
        }

        pool.shutdown();


        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

    private static DirectedGraph generateRandomHamiltonian(int size) {
        DirectedGraph graph = new DirectedGraph(size);

        List<Integer> nodes = graph.getNodes();

        Collections.shuffle(nodes);

        for (int i = 1; i < nodes.size(); i++){
            graph.addEdge(nodes.get(i - 1),  nodes.get(i));
        }

        graph.addEdge(nodes.get(nodes.size() -1), nodes.get(0));

        Random random = new Random();

        for (int i = 0; i < size / 2; i++){
            int nodeA = random.nextInt(size - 1);
            int nodeB = random.nextInt(size - 1);

            graph.addEdge(nodeA, nodeB);
        }

        return graph;
    }

}
