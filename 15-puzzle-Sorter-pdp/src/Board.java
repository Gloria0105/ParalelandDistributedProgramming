import java.util.ArrayDeque;
import java.util.Queue;

class Board {
    int[][] array;
    int N;
    int emptyRow;
    int emptyCol;
    boolean reached;
    int manhattan = 0;

    public Board(int[][] blocks) {
        N = blocks.length;
        array = new int[N][N];
        reached = true;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                array[i][j] = blocks[i][j];
                if (array[i][j] == 0) {
                    // search for the position of the empty block(0)
                    emptyRow = i;
                    emptyCol = j;
                }
                if (array[i][j] != N * i + j + 1) {
                    if (!(i == N - 1 && j == N - 1)) {
                        reached = false;
                    }
                }
                int num = array[i][j];
                if (num == 0) {
                    continue;
                }
                int indManhattan = Math.abs(Solver.correctRow[num - 1] - i)
                        + Math.abs(Solver.correctCol[num - 1] - j);
                // calculates the total manhattan distance for the entire board
                manhattan += indManhattan;
            }
        }
    }

    public int dimension() {
        return N;
    }


    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return reached;
    }


    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.array.length != this.array.length) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (that.array[i][j] != this.array[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> q = new ArrayDeque<Board>();
        int firstIndex0 = 0;
        int secondIndex0 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (array[i][j] == 0) {
                    firstIndex0 = i;
                    secondIndex0 = j;
                    break;
                }
            }
        }
        // verify if the index where you want to move the 0 is outside the board and if not put every copy of the board
        // with the 0 moved in all positions possible (4 of them in the best case) in the queue
        if (secondIndex0 - 1 > -1) {
            int[][] newArr = getCopy();
            exchange(newArr, firstIndex0, secondIndex0, firstIndex0, secondIndex0 - 1);
            q.add(new Board(newArr));
        }
        if (secondIndex0 + 1 < N) {
            int[][] newArr = getCopy();
            exchange(newArr, firstIndex0, secondIndex0, firstIndex0, secondIndex0 + 1);
            q.add(new Board(newArr));
        }
        if (firstIndex0 - 1 > -1) {
            int[][] newArr = getCopy();
            exchange(newArr, firstIndex0, secondIndex0, firstIndex0 - 1, secondIndex0);
            q.add(new Board(newArr));
        }
        if (firstIndex0 + 1 < N) {
            int[][] newArr = getCopy();
            exchange(newArr, firstIndex0, secondIndex0, firstIndex0 + 1, secondIndex0);
            q.add(new Board(newArr));
        }
        return q;
    }

    private int[][] getCopy() {
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(array[i], 0, copy[i], 0, N);
        }
        return copy;
    }

    private void exchange(int[][] arr, int firstIndex, int secIndex, int firstIndex2, int secIndex2) {
        // exchange the values from the element on the fistIndex and secIndex with the element from firstIndex2 and secIndex2
        int temp = arr[firstIndex][secIndex];
        arr[firstIndex][secIndex] = arr[firstIndex2][secIndex2];
        arr[firstIndex2][secIndex2] = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%4d", array[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

}
