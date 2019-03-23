import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

public class Test {

    public static void main(String[] args) {
        ChiSquareTest test = new ChiSquareTest();
        long[][] stats = new long[][]{
                {19, 31}, {76, 489}
                //{6,99},{22,200}
        };
        double result = test.chiSquareTest(stats);
        System.out.println(result);

        NormalDistribution dis = new NormalDistribution(1.340447, 1.074329);
        double cp= dis.cumulativeProbability(1);

        dis = new NormalDistribution(14.14854, 8.025591);
        cp= dis.cumulativeProbability(18);
        System.out.println(cp);

    }
}
