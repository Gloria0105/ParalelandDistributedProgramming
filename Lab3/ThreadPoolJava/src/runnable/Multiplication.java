package runnable;

import models.Matrix;

import java.util.List;

public class Multiplication implements Runnable {
    Matrix firstMatrix;
    Matrix secondMatrix;
    Matrix resultMatrix;
    int indexOfThred;


    public Multiplication(Matrix a, Matrix b, Matrix result, int indexOfThred) {
        this.firstMatrix = a;
        this.secondMatrix = b;
        this.resultMatrix = result;
        this.indexOfThred = indexOfThred;

    }

    @Override
    public void run() {
        Operations matrixOperations = new Operations(firstMatrix, secondMatrix);


        // Compute partial result by rows
//        List<Integer> resultByRows = matrixOperations.partialResultByRows(indexOfThred, 0, indexOfThred, firstMatrix.getColsNumber() - 1, resultMatrix.getRow(indexOfThred));
//        resultMatrix.setRow(indexOfThred, resultByRows);

        // Compute partial result by columns
        // List<Integer> resultByColumns = matrixOperations.partialResultByColumns(0, indexOfThred, firstMatrix.getColsNumber() - 1, indexOfThred, resultMatrix.getCol(indexOfThred));
        // resultMatrix.setCol(threadIndex, resultByColumns);

        // Compute partial result by K element
         List<Integer> resultByKElement = matrixOperations.partialResultByKElement(firstMatrix.getRowsNumber(), indexOfThred, resultMatrix.getRow(indexOfThred));
         resultMatrix.setRow(indexOfThred, resultByKElement);

    }

}
