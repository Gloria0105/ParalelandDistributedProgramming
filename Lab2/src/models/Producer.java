package models;



public class Producer extends Thread {
		private Shared list;
		public Producer(Shared list){
				this.list=list;
		}
	@Override
	public void run()
	{
			try {
					list.produce();
			}
			catch (InterruptedException e) {
					e.printStackTrace();
			}
	}
}
