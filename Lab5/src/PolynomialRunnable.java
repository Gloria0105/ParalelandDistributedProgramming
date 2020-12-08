public class PolynomialRunnable implements Runnable{
    private int start;
    private int end;
    private Polynomial polynomial1, polynomial2, result;

    public PolynomialRunnable(int start, int end, Polynomial polynomial1, Polynomial polynomial2, Polynomial result) {
        this.start = start;
        this.end = end;
        this.polynomial1 = polynomial1;
        this.polynomial2 = polynomial2;
        this.result = result;
    }

    @Override
    public void run() {
        for (int index = start; index < end; index++) {
            //case - no more elements to calculate
            if (index > result.getLength()) {
                return;
            }
            //find all the pairs that we add to obtain the value of a result coefficient
            for (int j = 0; j <= index; j++) {
                if (j < polynomial1.getLength() && (index - j) < polynomial2.getLength()) {
                    int value = polynomial1.getCoefficients().get(j) * polynomial2.getCoefficients().get(index - j);
                    result.getCoefficients().set(index, result.getCoefficients().get(index) + value);
                }
            }
        }
    }
}
