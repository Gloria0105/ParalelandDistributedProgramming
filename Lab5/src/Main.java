public class Main {
     public static void main(String[] args) {
         Polynomial p = new Polynomial(8000);
         Polynomial q = new Polynomial(8000);

//         System.out.println("pol p:" + p);
//         System.out.println("\n");
//         System.out.println("pol q" + q);
//         System.out.println("\n");

         //Simple multiplication sequential
         System.out.println("Simple sequential multiplication of polynomials: ");
         long startTime = System.currentTimeMillis();
         PolynomialMultiplication.multiplicationSequential(p, q);
         long endTime = System.currentTimeMillis();
         System.out.println("Execution time : " + (endTime - startTime) + " ms"+"\n");

         //Karatsuba multiplication sequential
         System.out.println("Karatsuba sequential multiplication of polynomials: ");
         long startTime1 = System.currentTimeMillis();
         PolynomialMultiplication.multiplicationKaratsubaSequential(p, q);
         long endTime1 = System.currentTimeMillis();
         System.out.println("Execution time : " + (endTime1 - startTime1) + " ms");
    }
}
