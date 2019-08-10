import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

import java.util.*;

public class Test {

    public static void main1(String[] args) {
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

    public static void main(String[] args) {
        List<String> horseNoList =new ArrayList<>();
        horseNoList.add("9");
        horseNoList.add("8");
        horseNoList.add("7");
        horseNoList.add("6");

        List<AnalyisResult> analyisResultList = new ArrayList<>();
        analyisResultList.add(new AnalyisResult("9","8",1));
        analyisResultList.add(new AnalyisResult("9","7",2));
        analyisResultList.add(new AnalyisResult("9","6",1));
        analyisResultList.add(new AnalyisResult("8","7",2));
        analyisResultList.add(new AnalyisResult("8","6",2));
        analyisResultList.add(new AnalyisResult("7","6",1));




        Map<String,Map<String,Integer>> analyisResultMap = new HashMap<>();
        for(AnalyisResult aResult :analyisResultList){
            aResult.prepare();
            aResult.print();
            String horseNoA = aResult.getHorseNoA();
            String horseNoB = aResult.getHorseNoB();
            Map<String,Integer> aMap =null;
            if(analyisResultMap.containsKey(horseNoA)){
                aMap = analyisResultMap.get(horseNoA);
            }else {
                aMap = new HashMap<>();
            }
            aMap.put(horseNoB,aResult.getCompareResult());
            analyisResultMap.put(horseNoA,aMap);
        }

        Collections.sort(horseNoList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                boolean needSwap=false;
                String lower = o1;
                String upper = o2;
                if(o1.compareTo(o2) > 0){
                    needSwap =true;
                    upper = o1;
                    lower = o2;
                }
                System.out.println("comparing "+ o1 +":"+o2);
                int result =0;

                if(analyisResultMap.get(lower)!=null && analyisResultMap.get(lower).get(upper) !=null) {
                    result =   analyisResultMap.get(lower).get(upper);
                    result = result ==2? 1 : -1;
                    if(needSwap){
                        result = -1* result;
                    }
                    return result;
                }

                return 0;
            }
        });

        System.out.println(horseNoList);
    }
     public static class AnalyisResult{
        private String horseNoA;
        private String horseNoB;
        private int compareResult;

        public AnalyisResult(String horseNoA, String horseNoB, int compareResult) {
            this.horseNoA = horseNoA;
            this.horseNoB = horseNoB;
            this.compareResult = compareResult ;
        }
        public void prepare(){
            if(horseNoA.compareTo(horseNoB)> 0){
                String temp = horseNoB;
                this.horseNoB = this.horseNoA;
                this.horseNoA = temp;
                this.compareResult =  this.compareResult==1? 2:1;
            }

        }


        public String getHorseNoA() {
            return horseNoA;
        }


        public String getHorseNoB() {
            return horseNoB;
        }

        public int getCompareResult() {
            return compareResult;
        }


         public void print() {
            System.out.println("AnalyisResult{" +
                     "horseNoA='" + horseNoA + '\'' +
                     ", horseNoB='" + horseNoB + '\'' +
                     ", compareResult=" + compareResult +
                     '}');
         }
     }
}
