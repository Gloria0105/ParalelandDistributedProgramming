package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Shared {

    Lock mutex = new ReentrantLock();
    LinkedList<Integer> products = new LinkedList<>();
    List<Integer> list1;
    List<Integer> list2;
    List<Integer> sums = new ArrayList<>();
    Integer sum = 0;

    private final Condition bufferNotFull = mutex.newCondition();
    private final Condition bufferNotEmpty = mutex.newCondition();

    public Shared(List<Integer> list1, List<Integer> list2) {

        this.list1 = list1;
        this.list2 = list2;
    }

    int capacity = 6;


    // Function called by producer thread
    public void produce(Integer product) throws InterruptedException {

        mutex.lock();
        try {
            while (products.size() == capacity) {
                bufferNotEmpty.await();
            }

            boolean isAdded = products.add(product);
            if (isAdded) {
                System.out.println("Producer produced-"
                        + product);
                bufferNotFull.signalAll();
            }

        } finally {
            mutex.unlock();

        }
    }

    public Integer consume() throws InterruptedException {
        mutex.lock();
        try {

            while (products.size() == 0) {
                bufferNotFull.await();
            }
                Integer value = products.removeFirst();
                if (value != null) {
                    return value;
                }
                bufferNotEmpty.signalAll();



        } finally {
            mutex.unlock();
        }
        return null;
    }
}
