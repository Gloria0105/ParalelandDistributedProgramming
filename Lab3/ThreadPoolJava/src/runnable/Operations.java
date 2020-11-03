package runnable;

import models.Matrix;

import java.util.List;

 public class Operations {

    Matrix a;
    Matrix b;


    Operations(Matrix a, Matrix b) {
        this.a = a;
        this.b = b;

    }

    public int computeOneElement(int row, int column) {
        int result = 0;
        for (int i = 0; i < a.getColsNumber(); i++) {
            result += a.get(row, i) * b.get(i, column);
        }
        return result;
    }

    public List<Integer> partialResultByRows(int startRowA, int startRowB, int endRowA, int endRowB, List<Integer> rowResult) {
        for (int i = startRowA; i <= endRowA; i++) {
            int firstColumn = i == startRowA ? startRowB : 0;
            int lastColumn = i == endRowA ? endRowB : a.getColsNumber() - 1;
            for (int j = firstColumn; j <= lastColumn; j++) {
                int element = computeOneElement(i, j);
                rowResult.set(j, element);
            }
        }
        return rowResult;
    }

    public List<Integer> partialResultByColumns(int startRowA, int startRowB, int endRowA, int endRowB, List<Integer> colResult) {
        for (int j = startRowB; j <= endRowB; j++) {
            int firstRow = j == startRowB ? startRowA : 0;
            int lastRow = j == endRowB ? endRowA : a.getRowsNumber() - 1;
            for (int i = firstRow; i <= lastRow; i++) {
                int element = computeOneElement(i, j);
                colResult.set(i, element);
            }
        }
        return colResult;
    }

    public List<Integer> partialResultByKElement(int k, int noOfThreds, List<Integer> kResult) {
        for (int i = 0; i < a.getRowsNumber(); i++) {
            for (int j = 0; j < a.getColsNumber(); j++) {
                if (index(i, j) % k == noOfThreds) {
                    int element = computeOneElement(i, j);
                    kResult.set(i, element);
                }
            }
        }
        return kResult;
    }

    private int index(int row, int col) {
        return row * a.getColsNumber() + col;
    }

    public Matrix getMatrixA() {
        return a;
    }

    public Matrix getMatrixB() {
        return b;
    }

}
