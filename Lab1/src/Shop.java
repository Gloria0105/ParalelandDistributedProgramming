import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class Shop extends Thread {
		private Long id;
		private Inventory inventory;
		private List<Bill> bills;
		private Integer leftQuantity = 0;
		public Integer bought = 0;
		public Lock _mutex;
		private Inventory initialInventory = new Inventory();

		public void setBought(Integer bought) {
				this.bought = bought;
		}

		@Override
		public long getId() {
				return id;
		}

		public Integer getLeftQuantity() {
				return leftQuantity;
		}

		public List<Bill> getBills() {
				return bills;
		}


		public Shop(Inventory inventory, Long id, List<Bill> bills, Lock _mutex) {
				this.inventory = inventory;
				this.id = id;
				this.bills = bills;
				this.initialInventory.setProducts((HashMap<Product, Integer>) this.inventory.getP().clone());
				this._mutex = _mutex;
		}

		@Override
		public void run() {
				for (Bill b : bills) {
						buyProducts(b);
				}
		}

		public Integer verifyAsItGoes() {
				Integer initialQ = initialInventory.getQuantities();
				Integer onTheWayQ = inventory.getQuantities();
				setBought((initialQ - onTheWayQ));
				return (initialQ - onTheWayQ);
		}

		private void buyProducts(Bill b) {
				Integer quantity = b.getQuantity();
				while (quantity > 0) {
						_mutex.lock();
						try {
								inventory.remove(b.getProduct(), 1);
								this.bought = this.bought + 1;
								quantity--;
								//System.out.println(this.id + ": took " + b.getProduct().getType() + " -> " + String.valueOf(1));
						} catch (Exception e) {
								System.out.println(e.getMessage());
								_mutex.unlock();
								break;
						}
						this.leftQuantity = quantity;
						_mutex.unlock();

				}
		}
}
