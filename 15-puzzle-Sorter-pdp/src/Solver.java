import java.io.InputStreamReader;
import java.util.*;

class Solver {
    public int minMoves;
    public static int[] correctRow;
    public static int[] correctCol;
    PriorityQueue<Node> pq = new PriorityQueue<Node>();

    public Node lastNode;
    public boolean solvable;

    public Solver(Board initial) {
        pq.add(new Node(initial, 0, null));
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return minMoves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> stack = new Stack<Board>();
        Node node = lastNode;
        while (node != null) {
            Board board = node.board;
            node = node.prevNode;
            stack.push(board);
        }
        return stack;
    }

    static void initCorrectRowsCols(int N) {
        correctRow = new int[N * N];
        int z = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                correctRow[z++] = i;
            }
        }
        z = 0;
        correctCol = new int[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                correctCol[z++] = j;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // create initial board from file
        Scanner in = new Scanner(new InputStreamReader(System.in));
        int N = 4;
        initCorrectRowsCols(N);
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.nextInt();

        long start = System.currentTimeMillis();

        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        Node removed = solver.pq.poll();
        assert removed != null;
        Iterable<Board> neighbors = removed.board.neighbors();
        for (Board board : neighbors) {
            if (removed.prevNode != null && removed.prevNode.board.equals(board)) {
                continue;
            }

            solver.pq.add(new Node(board, removed.moves + 1, removed));
        }
        int i = solver.pq.size();
        List<Operation> threadList = new ArrayList<>();
        while (i != 0) {
            Node remove1 = solver.pq.poll();
            Operation thread = new Operation(remove1);
            threadList.add(thread);
            i--;
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            thread.join();
        }
        long end = System.currentTimeMillis();

        System.out.println("time taken " + (end - start)  + " milliseconds");
        System.out.println("Number of threads " + threadList.size());
        int minMoves = 9999;
        for (Operation thread : threadList) {
            if (thread.getMinMoves() < minMoves)
                minMoves = thread.getMinMoves();
        }

        for (Operation thread : threadList) {
            if (thread.getMinMoves() == minMoves) {
                solver.minMoves = thread.minMoves;
                solver.solvable = thread.solvable;
                solver.pq = thread.pq;
                solver.lastNode = thread.lastNode;
            }
        }
        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            Stack<Board> stack = new Stack<Board>();
            for (Board board : solver.solution())
                stack.push(board);

            while (!stack.isEmpty()) {
                System.out.println(stack.pop());
                System.out.println(stack.size());
            }
        }
    }

}
