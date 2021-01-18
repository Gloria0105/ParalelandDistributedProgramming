import mpi.MPI;

import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;

class Solver {
    private static int minMoves;
    public static int[] correctRow;
    public static int[] correctCol;


    private static Node lastNode;
    private static boolean solvable;
    private static final PriorityQueue<Node> pq = new PriorityQueue<Node>();

    public Solver(Board initial) {
        int size = MPI.COMM_WORLD.Size();
        System.out.println("size: " + size);
        long start = System.currentTimeMillis();
        pq.add(new Node(initial, 0, null));
        Node removed = pq.poll();
        assert removed != null;
        Iterable<Board> neighbors = removed.board.neighbors();
        for (Board board : neighbors) {
            if (removed.prevNode != null && removed.prevNode.board.equals(board)) {
                continue;
            }
            pq.add(new Node(board, removed.moves + 1, removed));
        }
        while (!isSolvable()) {
            for (int i = 0; i < pq.size(); i++) {
                // poll returns the element at the head of the Queue else returns NULL if the Queue is empty.
                Node removed1 = pq.poll();

                MPI.COMM_WORLD.Send(new boolean[]{false}, 0, 1, MPI.BOOLEAN, i + 1, 0);
                MPI.COMM_WORLD.Send(new Object[]{removed1}, 0, 1, MPI.OBJECT, i + 1, 0);
                MPI.COMM_WORLD.Send(new int[]{removed1.moves}, 0, 1, MPI.INT, i + 1, 0);

            }

            Object[] node = new Object[1];
            MPI.COMM_WORLD.Recv(node, 0, 1, MPI.OBJECT, 1, 0);


            // receive data
            Node nodeeee = (Node) node[0];

            if (nodeeee.board.isGoal()) {
                long end = System.currentTimeMillis();
                System.out.println("time taken " + (end - start) + " milli seconds");
                solvable = true;
                lastNode = nodeeee;
                minMoves = nodeeee.moves;

                // print solution to standard output

                System.out.println("Minimum number of moves = " + moves());
                Stack<Board> stack = new Stack<Board>();

                for (Board board : solution())
                    stack.push(board);
                while (!stack.isEmpty()) {
                    System.out.println(stack.pop());
                    System.out.println(stack.size());
                }
                for (int i = 1; i < size; i++) {
                    // shut down workers when solution was found
                    Node curr = pq.poll();
                    MPI.COMM_WORLD.Send(new boolean[]{true}, 0, 1, MPI.BOOLEAN, i, 0);
                    MPI.COMM_WORLD.Send(new Object[]{curr}, 0, 1, MPI.OBJECT, i, 0);
                    MPI.COMM_WORLD.Send(new int[]{minMoves}, 0, 1, MPI.INT, i, 0);
                }
                break;
            }
        }


    }

    private static void findSolution() {

        while (true) {
            Object[] removed = new Object[1];
            int[] minMoves = new int[1];
            boolean[] solvable = new boolean[1];
            MPI.COMM_WORLD.Recv(solvable, 0, 1, MPI.BOOLEAN, 0, 0);
            MPI.COMM_WORLD.Recv(removed, 0, 1, MPI.OBJECT, 0, 0);
            MPI.COMM_WORLD.Recv(minMoves, 0, 1, MPI.INT, 0, 0);
            if (solvable[0]) {
                return;
            }
            Node current = (Node) removed[0];
            Node result = find(current);
            MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);


        }

    }

    private static Node find(Node current) {
        while (true) {
            assert current != null;
            if (current.board.isGoal()) {
                minMoves = current.moves;
                lastNode = current;
                solvable = true;
                break;
            }


            Iterable<Board> neighbors = current.board.neighbors();
            for (Board board : neighbors) {
                if (current.prevNode != null && current.prevNode.board.equals(board)) {
                    continue;
                }
                pq.add(new Node(board, current.moves + 1, current));
            }
            current = pq.poll();
        }
        return lastNode;

    }


    public static boolean isSolvable() {
        return solvable;
    }

    public static int moves() {
        return minMoves;
    }

    public static Iterable<Board> solution() {
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

    public static void main(String[] args) throws FileNotFoundException {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int N = 4;
        initCorrectRowsCols(N);
        if (me == 0) {
            Board initial = Board.fromFile();
            Solver solver = new Solver(initial);
        } else {
            findSolution();
        }

        MPI.Finalize();
    }
}
