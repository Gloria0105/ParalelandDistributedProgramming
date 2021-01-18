class Node implements Comparable<Node> {
    Board board;
    int moves;
    Node prevNode;

    public Node(Board board, int moves, Node prev) {
        this.board = board;
        this.moves = moves;
        this.prevNode = prev;
    }

    public int compareTo(Node node) {
        // returns -1 if node priority is bigger than this priority
        // returns 0 if node priority is equal to  this priority
        // returns 1 if node priority is less than this priority

        int thisPriority = this.moves + this.board.manhattan();
        int nodePriority = node.moves + node.board.manhattan();
        return Integer.compare(thisPriority, nodePriority);
    }
}
