package main;

import models.Consumer;
import models.Producer;
import models.Shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
		public static void main(String[] args) throws InterruptedException {
				List<Integer> list1= new ArrayList<>();
				List<Integer> list2= new ArrayList<>();
				list1.add(1);
				list2.add(1);
				list1.add(2);
				list2.add(2);
				list1.add(3);
				list2.add(3);
				Integer noOfVal = 3;
				 final Shared shared=new Shared(list1,list2);
				 Producer producer=new Producer(list1,list2,shared);
				Consumer consumer = new Consumer(shared,noOfVal);

				producer.start();
				consumer.start();

				producer.join();
				consumer.join();

		}
}
