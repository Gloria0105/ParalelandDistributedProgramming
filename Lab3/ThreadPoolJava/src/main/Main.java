package main;

import models.Matrix;
import runnable.Multiplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {



    public static void main(String[] args) throws Exception {

        Matrix first = new Matrix(6, 6);
        Matrix second = new Matrix(6, 6);

        Matrix result = new Matrix(6, 6);

        float start = System.nanoTime() / 1000000;
        List<Multiplication> tasks = new ArrayList<>();
        int noOfThreads = first.getColsNumber();
        for (int i = 0; i < noOfThreads; i++) {
            tasks.add(new Multiplication(first, second, result, i));
        }

        ExecutorService pool = Executors.newFixedThreadPool(noOfThreads);

        for (Multiplication task : tasks) {
            pool.execute(task);
        }

        pool.shutdown();



        float end = System.nanoTime() / 1000000;



        System.out.println("Execution time: " + (end - start) + " ms.");
    }
}
