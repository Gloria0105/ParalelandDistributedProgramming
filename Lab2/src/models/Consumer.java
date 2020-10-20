package models;

public class Consumer extends Thread{
		private Shared list;
		public Consumer(Shared list){
				this.list=list;
		}
		@Override
		public void run()
		{
				try {
						list.consume();
				}
				catch (InterruptedException e) {
						e.printStackTrace();
				}
		}
}
