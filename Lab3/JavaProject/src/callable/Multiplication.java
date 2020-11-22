package callable;
import callable.Operations;
import models.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Multiplication implements Callable {
    Matrix firstMatrix;
    Matrix secondMatrix;
    Matrix resultMatrix;
    int indexOfThred;
    private static Lock mutex = new ReentrantLock();


    public Multiplication(Matrix a, Matrix b, Matrix result, int indexOfThred) {
        this.firstMatrix = a;
        this.secondMatrix = b;
        this.resultMatrix = result;
        this.indexOfThred = indexOfThred;

    }
    @Override
    public Integer call()  {
        Operations matrixOperations = new Operations(firstMatrix,secondMatrix);

        mutex.lock();

        // Compute partial result by rows
//        List<Integer> resultByRows = matrixOperations.partialResultByRows(indexOfThred, 0, indexOfThred, firstMatrix.getColsNumber() - 1, resultMatrix.getRow(indexOfThred));
//        resultMatrix.setRow(indexOfThred, resultByRows);

        // Compute partial result by columns
        // List<Integer> resultByColumns = matrixOperations.partialResultByColumns(0, indexOfThred, firstMatrix.getColsNumber() - 1, indexOfThred, resultMatrix.getCol(indexOfThred));
        // resultMatrix.setCol(threadIndex, resultByColumns);

         //Compute partial result by K element
         List<Integer> resultByKElement = matrixOperations.partialResultByKElement(firstMatrix.getRowsNumber(), indexOfThred, resultMatrix.getRow(indexOfThred));
         resultMatrix.setRow(indexOfThred, resultByKElement);

        mutex.unlock();

        return 0;
    }

}

