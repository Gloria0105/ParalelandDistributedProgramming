package models;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Shared {

    Lock mutex = new ReentrantLock();
    Queue<Integer> products = new LinkedList<>();
    HashMap<Integer, Integer> listOfPairs;
    List<Integer> sums = new ArrayList<>();
    Integer sum = 0;

    private final Condition bufferNotFull = mutex.newCondition();
    private final Condition bufferNotEmpty = mutex.newCondition();

    public Shared(HashMap<Integer, Integer> listOfPairs) {
        this.listOfPairs = listOfPairs;
    }

    int capacity = 6;


    // Function called by producer thread
    public void produce() throws InterruptedException {

        mutex.lock();
        try {
            if (products.size() == capacity) {
                bufferNotEmpty.await();
            }

            for (Map.Entry<Integer, Integer> a : listOfPairs.entrySet()) {
                Integer value = a.getValue();
                Integer key = a.getKey();
                Integer product = value * key;
                products.offer(product);

                System.out.println("Producer produced-"
                        + product);
                bufferNotFull.signalAll();

            }
        } finally {
            mutex.unlock();

        }
        // makes the working of program easier
        // to  understand


    }

    public void consume() throws InterruptedException {
        mutex.lock();
        try {

            while (products.size() == 0) {
                bufferNotFull.await();
            }
            while (!products.isEmpty()) {
                Integer value = products.poll();
                if (value != null) {
                    sum += value;
                    sums.add(sum);
                    System.out.println("Consumer consumed-"
                            + sum);
                    bufferNotEmpty.signalAll();
                }
            }


        } finally {
            mutex.unlock();
        }

    }
}
