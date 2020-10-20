package models;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Shared {
		// Create a list shared by producer and consumer
		// Size of list is 2.
		Lock mutex = new ReentrantLock();
		List<Integer> products=new ArrayList<>();
		HashMap<Integer,Integer>listOfPairs;
		public Shared(HashMap<Integer,Integer>listOfPairs){
				this.listOfPairs=listOfPairs;
		}
		int capacity = 6;

		// Function called by producer thread
		public void produce() throws InterruptedException
		{


								// producer thread waits while list
								// is full
								while (listOfPairs.size() == capacity) {
										wait();
								}
								for( Map.Entry a :listOfPairs.entrySet()) {
										Integer value = (Integer) a.getValue();
										Integer key = (Integer) a.getKey();
										Integer product = value * key;
										mutex.lock();
										products.add(product);
										System.out.println("Producer produced-"
														+ product);
										mutex.unlock();
								}


								// makes the working of program easier
								// to  understand


		}
		public void consume() throws InterruptedException {


								// consumer thread waits while list
								// is empty
								while (products.size() <= 1) {
										wait();
								}
								// to retrive the ifrst job in the list
								Integer sum = 0;
									mutex.lock();
								for(Integer product : products){
										sum+=product;
										System.out.println("Consumer consumed-"
														+ sum);
								}

							mutex.unlock();


				}

}
