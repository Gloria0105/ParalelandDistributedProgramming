import java.util.concurrent.ExecutionException;

public class Main {
     public static void main(String[] args) throws InterruptedException, ExecutionException {
         Polynomial polynomial1 = new Polynomial(5000);
         Polynomial polynomial2 = new Polynomial(5000);

//         System.out.println("pol p:" + p);
//         System.out.println("\n");
//         System.out.println("pol q" + q);
//         System.out.println("\n");

         //Simple multiplication sequential
         System.out.println("Simple sequential multiplication of polynomials: ");
         long startTime = System.currentTimeMillis();
         PolynomialMultiplication.multiplicationSequential(polynomial1, polynomial2);
         long endTime = System.currentTimeMillis();
         System.out.println("Execution time : " + (endTime - startTime) + " ms"+"\n");

         //Karatsuba multiplication sequential
         System.out.println("Karatsuba sequential multiplication of polynomials: ");
         long startTime1 = System.currentTimeMillis();
         PolynomialMultiplication.multiplicationKaratsubaSequential(polynomial1, polynomial2);
         long endTime1 = System.currentTimeMillis();
         System.out.println("Execution time : " + (endTime1 - startTime1) + " ms"+"\n");

         //Simple multiplication parallelized
         System.out.println("Simple sequential multiplication of polynomials: ");
         long startTime2 = System.currentTimeMillis();
         PolynomialMultiplication.multiplicationParallelized(polynomial1, polynomial2,4);
         long endTime2 = System.currentTimeMillis();
         System.out.println("Execution time : " + (endTime2 - startTime2) + " ms"+"\n");

         //Karatsuba multiplication parallelized
         System.out.println("Karatsuba sequential multiplication of polynomials: ");
         long startTime3 = System.currentTimeMillis();
         PolynomialMultiplication.multiplicationKaratsubaParallelized(polynomial1, polynomial2,4);
         long endTime3 = System.currentTimeMillis();
         System.out.println("Execution time : " + (endTime3 - startTime3) + " ms");
    }
}
