import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {


		public static void main(String[] args) throws InterruptedException {
				 Lock _mutex = new ReentrantLock();
				float start =  System.nanoTime() / 1000000;
				Product p1= new Product("A",0.2);
				Product p2= new Product("B",0.2);
				Product p3= new Product("C",0.2);
				Product p4= new Product("D",0.2);
				Product p5= new Product("E",0.2);
				Product p6= new Product("F",0.2);
				Inventory inventory =new Inventory();
				Inventory initialInventory =new Inventory();
				inventory.add(p1,100000000);
				inventory.add(p2,211223123);
				inventory.add(p3,11000000);
				inventory.add(p4,109223);
				inventory.add(p5,200880000);
				inventory.add(p6,5055655);

				initialInventory.add(p1,100000000);
				initialInventory.add(p2,211223123);
				initialInventory.add(p3,11000000);
				initialInventory.add(p4,109223);
				initialInventory.add(p5,200880000);
				initialInventory.add(p6,5055655);

				Integer initialQuantityShop1 = 1249;
				Integer initialQuantityShop2 = 5232;
				Integer initialQuantityShop3 = 123;
				Integer initialQuantityShop4 = 7333;

				List<Bill> bills1=new ArrayList<>();
				bills1.add(new Bill(p1,initialQuantityShop1));
				bills1.add(new Bill(p2,initialQuantityShop2));
				Shop shop1=new Shop(inventory,new Long(1),bills1,_mutex);

				List<Bill> bills2=new ArrayList<>();
				bills2.add(new Bill(p3,initialQuantityShop1));
				bills2.add(new Bill(p4,initialQuantityShop2));
				Shop shop2=new Shop(inventory,new Long(2),bills2,_mutex);

				List<Bill> bills3=new ArrayList<>();
				bills3.add(new Bill(p1,initialQuantityShop3));
				bills3.add(new Bill(p4,initialQuantityShop4));
				Shop shop3=new Shop(inventory,new Long(3),bills3,_mutex);

				List<Shop>shopList = new ArrayList<>();
				shopList.add(shop1);
				shopList.add(shop2);
				shopList.add(shop3);
				Verifier verifier =new Verifier(shopList);
				shop1.start();
				shop2.start();
				shop3.start();
				verifier.start();


				shop1.join();
				shop2.join();
				shop3.join();
				verifier.join();

				float end = System.nanoTime() / 1000000;

				System.out.println(initialInventory.toString());
				System.out.println(inventory.toString());
				System.out.println("Start: "+start);
				System.out.println("End: "+end);
				System.out.println("\nEnd work: " + (end - start) / 1000 + " seconds");
				verify(initialInventory,inventory,shopList);
				Test test= new Test();
				test.firstTest();
				test.secondTest();
				test.thirdTest();

		}

		static void verify(Inventory initialInventory,Inventory afterInventory,List<Shop>shops) {
				System.out.println("Verifying the stock...");
				Integer sum =0;
				for (Shop shop: shops){
						List<Bill> bills=shop.getBills();
						for(Bill bill:bills){
								sum+=bill.getQuantity();
						}

				}

				if(initialInventory.getQuantities()-afterInventory.getQuantities()<=sum)
						System.out.println("All good");
				else System.out.println("All bad");

		}

}
