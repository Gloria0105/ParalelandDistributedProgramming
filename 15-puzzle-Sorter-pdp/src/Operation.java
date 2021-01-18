import java.util.PriorityQueue;

public class Operation extends Thread {
    public int minMoves;

    public int getMinMoves() {
        return minMoves;
    }

    public boolean solvable;
    public Node lastNode;
    PriorityQueue<Node> pq = new PriorityQueue<Node>();


    public Operation(Node removed) {
        pq.add(removed);

    }

    @Override
    public void run() {
        while (true) {
            Node removed = pq.poll();
            assert removed != null;
            if (removed.board.isGoal()) {
                minMoves = removed.moves;
                lastNode = removed;
                solvable = true;
                break;
            }

            Iterable<Board> neighbors = removed.board.neighbors();

            for (Board board : neighbors) {
                if (removed.prevNode != null && removed.prevNode.board.equals(board)) {
                    continue;
                }

                pq.add(new Node(board, removed.moves + 1, removed));
            }

        }
    }
}
