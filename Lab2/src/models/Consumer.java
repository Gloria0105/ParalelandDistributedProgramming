package models;

public class Consumer extends Thread{
		private final Shared shared;
		Integer sum;
		Integer noOfValues;
		public Consumer(Shared shared,Integer numberOfValues){
				this.shared=shared;
				this.sum=0;
				this.noOfValues = numberOfValues;
		}
		@Override
		public void run()
		{
				while(noOfValues>0)
				try {
						Integer value = shared.consume();
						sum+=value;
						System.out.println("Consumer consumed- "+sum);
						noOfValues-=1;
						Thread.sleep(200);

				}
				catch (InterruptedException e) {
						e.printStackTrace();
				}
		}
}
