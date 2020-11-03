package main;

import callable.Multiplication;
import models.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class Main {


    public static void main(String[] args) {

        Matrix firstMatrix = new Matrix(4, 4);
        Matrix secondMatrix = new Matrix(4, 4);
        Matrix resultMatrix = new Matrix(4, 4);

        List<Multiplication> callables = new ArrayList<>();
        float start = System.nanoTime() / 1000000;

        int noOfThred = firstMatrix.getColsNumber();
        for (int i = 0; i < noOfThred; i++) {
            callables.add(new Multiplication(firstMatrix, secondMatrix, resultMatrix, i));
        }


        List<FutureTask> tasks = new ArrayList<>();

        for (Multiplication callable : callables) {
            tasks.add(new FutureTask(callable));
        }

        for (FutureTask task : tasks) {
            Thread thread = new Thread(task);
            thread.start();
        }


        for (FutureTask task : tasks) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        float end = System.nanoTime() / 1000000;


        System.out.println("Execution time: " + (end - start) + " ms.");
    }
}
