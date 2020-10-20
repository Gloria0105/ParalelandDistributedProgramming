import java.util.HashMap;
import java.util.Set;


public class Inventory {
		private HashMap<Product, Integer> products;

		public void setProducts(HashMap<Product, Integer> products) {
				this.products = products;
		}

		public Inventory() {
				this.products = new HashMap<>();
		}
		public void add(Product product,int quantity){
				if (this.products.containsKey(product)){
						this.products.replace(product, this.products.get(product) + quantity);
				}else{
						this.products.put(product, quantity);
				}
		}
		public Set<Product> getProducts(){
				return this.products.keySet();
		}
		public HashMap<Product,Integer>getP(){return products;}
		public int getQuantities() {
				int quantity=0;
				for(Product p: this.products.keySet()){
						quantity+=getQuantity(p);
				}
				return quantity;
		}
		public int getQuantity(Product product) {
				return this.products.get(product);
		}
		protected void remove(Product product, int quantity) {

				if (this.products.containsKey(product)){
						if (this.getQuantity(product) < quantity){
								throw new RuntimeException("Cannot remove desired quantity from inventory, existing quantity is not sufficient!");
						}
						this.products.replace(product, this.products.get(product) - quantity);
						if (this.getQuantity(product) == 0){
								this.products.remove(product);
						}

				}else{
						throw new RuntimeException("Cannot remove quantity of inexistent product in inventory!");
				}
		}

		@Override
		public String toString() {
				return "Inventory{" +
								"products=" + products.toString()+
								'}';
		}

}
