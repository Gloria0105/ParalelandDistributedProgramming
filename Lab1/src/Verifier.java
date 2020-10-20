import java.util.List;

public class Verifier extends Thread {
		public List<Shop> shopList;
		int bought = 0;
		int difference = 0;
		boolean ok = false;

		public Verifier(List<Shop> shopList) {
				this.shopList = shopList;
		}

		public boolean verify() {
				bought = 0;
				difference = 0;
				shopList.get(0)._mutex.lock();
				for (Shop shop : shopList) {
						bought += shop.bought;
						difference += shop.verifyAsItGoes();
				}
				if (bought == difference) ok = true;
				else ok = false;

				shopList.get(0)._mutex.unlock();
				return ok;
		}

		public boolean verifyIfExistsThread() {
				for (Shop shop : shopList) {
						if (shop.isAlive()) {
								return true;
						}
				}
				return false;
		}

		@Override
		public void run() {
				ok = false;
				while (true) {
						if (verifyIfExistsThread()) ok = verify();
						else break;
				}
				if (ok) System.out.println("Verifications were made\nAll checks out");
		}
}
