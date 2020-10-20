import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
		public void firstTest() throws InterruptedException {
				float start =  System.nanoTime() / 1000000;
				Lock mutex = new ReentrantLock();
				Inventory inventory=new Inventory();
				List<Bill> bills=new ArrayList<>();
				List<Shop>shops =new ArrayList<>();
				for(int i=0;i<=100;i++){
						Product p= new Product(String.valueOf(i),i+0.2);

						inventory.add(p,1000000);
				}
				for(Product p:inventory.getProducts()){
						Bill bill =new Bill(p, 12);
						bills.add(bill);
				}
				for(int i=0;i<90;i++){
						List <Bill>billList =new ArrayList<>();
						billList.add(bills.get(i));
						billList.add(bills.get(i+1));
						billList.add(bills.get(i+2));
						billList.add(bills.get(i+3));
						Shop shop = new Shop(inventory,new Long(i),billList,mutex);
						shops.add(shop);
				}
				for(Shop shop:shops){
						shop.start();
				}
				for(Shop shop:shops){
						shop.join();
				}

				float end = System.nanoTime() / 1000000;
				System.out.println("Test1...");
				System.out.println(inventory.getProducts().size());
				System.out.println(shops.size());
				System.out.println("\nEnd work: " + (end - start) / 1000 + " seconds");
		}
		public void secondTest() throws InterruptedException {
				float start2 =  System.nanoTime() / 1000000;
				Lock mutex2 = new ReentrantLock();
				Inventory inventory2=new Inventory();
				List<Bill> bills2=new ArrayList<>();
				List<Shop>shops2 =new ArrayList<>();
				for(int i=0;i<=1000;i++){
						Product p= new Product(String.valueOf(i),i+0.2);

						inventory2.add(p,2300000);
				}
				for(Product p:inventory2.getProducts()){
						Bill bill =new Bill(p, 1200);
						bills2.add(bill);
				}
				for(int i=0;i<90;i++){
						List <Bill>billList2 =new ArrayList<>();
						billList2.add(bills2.get(i));
						billList2.add(bills2.get(i+1));
						billList2.add(bills2.get(i+2));
						billList2.add(bills2.get(i+3));
						billList2.add(bills2.get(i+5));
						billList2.add(bills2.get(i+9));
						billList2.add(bills2.get(i+7));
						billList2.add(bills2.get(i+123));
						Shop shop = new Shop(inventory2,new Long(i),billList2,mutex2);
						shops2.add(shop);
				}
				for(Shop shop:shops2){
						shop.start();
				}
				for(Shop shop:shops2){
						shop.join();
				}

				float end2= System.nanoTime() / 1000000;
				System.out.println("Test2...");
				System.out.println(inventory2.getProducts().size());
				System.out.println(shops2.size());
				System.out.println("\nEnd work: " + (end2 - start2) / 1000 + " seconds");
		}
		public void thirdTest() throws InterruptedException {

				float start3 =  System.nanoTime() / 1000000;
				Lock mutex3 = new ReentrantLock();
				Inventory inventory3=new Inventory();
				List<Bill> bills3=new ArrayList<>();
				List<Shop>shops3 =new ArrayList<>();
				for(int i=0;i<=10000;i++){
						Product p= new Product(String.valueOf(i),i+0.3);

						inventory3.add(p,121212);
				}
				for(Product p:inventory3.getProducts()){
						Bill bill =new Bill(p, 234);
						bills3.add(bill);
				}
				for(int i=0;i<900;i++){
						List <Bill>billList3 =new ArrayList<>();
						billList3.add(bills3.get(i));
						billList3.add(bills3.get(i+1));
						billList3.add(bills3.get(i+2));
						billList3.add(bills3.get(i+3));
						billList3.add(bills3.get(i+5));
						billList3.add(bills3.get(i+9));
						billList3.add(bills3.get(i+7));
						billList3.add(bills3.get(i+133));
						Shop shop = new Shop(inventory3,new Long(i),billList3,mutex3);
						shops3.add(shop);
				}
				for(Shop shop:shops3){
						shop.start();
				}
				for(Shop shop:shops3){
						shop.join();
				}

				float end3= System.nanoTime() / 1000000;
				System.out.println("Test3...");
				System.out.println(inventory3.getProducts().size());
				System.out.println(shops3.size());
				System.out.println("\nEnd work: " + (end3 - start3) / 1000 + " seconds");
		}
}
