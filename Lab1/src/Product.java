public class Product {
		private String type;
		private double unitPrice;

		public Product(String type, double unitPrice) {
				this.type = type;
				this.unitPrice = unitPrice;
		}

		public String getType() {
				return type;
		}

		public void setType(String type) {
				this.type = type;
		}

		public double getUnitPrice() {
				return unitPrice;
		}

		public void setUnitPrice(double unitPrice) {
				this.unitPrice = unitPrice;
		}

		@Override
		public String toString() {
				return "Product{" +
								"type='" + type + '\'' +
								", unitPrice=" + unitPrice +
								'}';
		}
}
