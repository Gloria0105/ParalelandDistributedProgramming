package models;


import java.util.List;

public class Producer extends Thread {
	private final List<Integer> list1;
	private final List<Integer> list2;
	Shared shared;

	public Producer(List<Integer> list1, List<Integer> list2, Shared shared) {
		this.list1 = list1;
		this.list2 = list2;
		this.shared = shared;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < list1.size(); i++) {
				Integer value1 = list1.get(i);
				Integer value2 = list2.get(i);
				Integer product = value1 * value2;
				shared.produce(product);
//				Thread.sleep(200);

			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
