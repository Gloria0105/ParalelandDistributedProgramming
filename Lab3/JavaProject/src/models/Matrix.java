package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrix {
    private List<List<Integer>> content;
    private int rows, cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        int max = 10;
        int min = 1;

        Random rand = new Random();

        this.content = new ArrayList<>(rows);
        for (int i = 0; i < this.rows; i++) {
            this.content.add(new ArrayList<>(cols));
            for (int j = 0; j < this.cols; j++) {
                this.content.get(i).add(rand.nextInt((max - min) + 1) + min);
            }
        }
    }

    public List<Integer> getRow(int index) {
        return this.content.get(index);
    }

    public List<Integer> getCol(int index) {
        List<Integer> col = new ArrayList<>();

        for (List<Integer> row : this.content) {
            col.add(row.get(index));
        }

        return col;
    }

    public void setRow(int rowNo, List<Integer> row) {

        this.content.set(rowNo, row);
    }

    public void setCol(int colNo, List<Integer> col) {

        for (int i = 0; i < col.size(); i++) {
            this.content.get(i).set(colNo, col.get(i));
        }
    }

    public int get(int row, int col) {
        return this.content.get(row).get(col);
    }

    public void set(int row, int col, int value) {
        this.content.get(row).set(col, value);
    }

    public int getRowsNumber() {
        return rows;
    }

    public int getColsNumber() {
        return cols;
    }

    public int index(int row, int col) {
        return row * this.cols + col;
    }

    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            ss.append(this.content.get(i).toString()).append("\n");
        }

        return ss.toString();
    }
}
