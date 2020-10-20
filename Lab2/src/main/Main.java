package main;

import models.Consumer;
import models.Producer;
import models.Shared;

import java.util.HashMap;

public class Main {
		public static void main(String[] args) throws InterruptedException {
				HashMap<Integer,Integer>values =new HashMap<>();
				values.put(1,1);
				values.put(2,2);
				values.put(3,3);
				values.put(4,4);
				values.put(5,5);
				values.put(6,6);
				 final Shared list=new Shared(values);
				 Producer producer=new Producer(list);
				Consumer consumer = new Consumer(list);

				producer.start();
				consumer.start();

				producer.join();
				consumer.join();

		}
}
