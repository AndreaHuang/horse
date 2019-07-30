package org.andrea.jockey.statistics;

import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceInfo;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatUtils {

    public static class SurvivalAnalysisResultWrapper{
        RaceInfo raceInfo;

        int drawA;
        String horseNoA;
        String horseIdA;
        String jockeyA;
        double finishTimeA=0.0;
        int drawB;
        String horseNoB;
        String horseIdB;
        String jockeyB;
        double finishTimeB=0.0;

        int result; //1 means A is better than B,2 means B is better than A

        public double getFinishTimeA() {
            return finishTimeA;
        }

        public void setFinishTimeA(double finishTimeA) {
            this.finishTimeA = finishTimeA;
        }

        public double getFinishTimeB() {
            return finishTimeB;
        }

        public void setFinishTimeB(double finishTimeB) {
            this.finishTimeB = finishTimeB;
        }



        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public RaceInfo getRaceInfo() {
            return raceInfo;
        }

        public void setRaceInfo(RaceInfo raceInfo) {
            this.raceInfo = raceInfo;
        }

        public int getDrawA() {
            return drawA;
        }

        public void setDrawA(int drawA) {
            this.drawA = drawA;
        }

        public String getHorseNoA() {
            return horseNoA;
        }

        public void setHorseNoA(String horseNoA) {
            this.horseNoA = horseNoA;
        }

        public String getHorseIdA() {
            return horseIdA;
        }

        public void setHorseIdA(String horseIdA) {
            this.horseIdA = horseIdA;
        }

        public String getJockeyA() {
            return jockeyA;
        }

        public void setJockeyA(String jockeyA) {
            this.jockeyA = jockeyA;
        }

        public int getDrawB() {
            return drawB;
        }

        public void setDrawB(int drawB) {
            this.drawB = drawB;
        }

        public String getHorseNoB() {
            return horseNoB;
        }

        public void setHorseNoB(String horseNoB) {
            this.horseNoB = horseNoB;
        }

        public String getHorseIdB() {
            return horseIdB;
        }

        public void setHorseIdB(String horseIdB) {
            this.horseIdB = horseIdB;
        }

        public String getJockeyB() {
            return jockeyB;
        }

        public void setJockeyB(String jockeyB) {
            this.jockeyB = jockeyB;
        }

        SurvivalAnalysisResult jockeyResult;
        SurvivalAnalysisResult drawResult;
        SurvivalAnalysisResult sameHorseResult;
        SurvivalAnalysisResult diffHorseResult;

        public SurvivalAnalysisResult getJockeyResult() {
            return jockeyResult;
        }

        public void setJockeyResult(SurvivalAnalysisResult jockeyResult) {
            this.jockeyResult = jockeyResult;
        }

        public SurvivalAnalysisResult getDrawResult() {
            return drawResult;
        }

        public void setDrawResult(SurvivalAnalysisResult drawResult) {
            this.drawResult = drawResult;
        }

        public SurvivalAnalysisResult getSameHorseResult() {
            return sameHorseResult;
        }

        public void setSameHorseResult(SurvivalAnalysisResult sameHorseResult) {
            this.sameHorseResult = sameHorseResult;
        }

        public SurvivalAnalysisResult getDiffHorseResult() {
            return diffHorseResult;
        }

        public void setDiffHorseResult(SurvivalAnalysisResult diffHorseResult) {
            this.diffHorseResult = diffHorseResult;
        }
    }

    public static class SurvivalAnalysisResult{
        BigDecimal confidence_interval_from;
        BigDecimal confidence_interval_to;
        BigDecimal relativeRisk_A2B;
        BigDecimal p;
        int countA;
        int countB;

        public int getCountA() {
            return countA;
        }

        public void setCountA(int countA) {
            this.countA = countA;
        }

        public int getCountB() {
            return countB;
        }

        public void setCountB(int countB) {
            this.countB = countB;
        }

        public BigDecimal getConfidence_interval_from() {
            return confidence_interval_from;
        }

        public void setConfidence_interval_from(BigDecimal confidence_interval_from) {
            this.confidence_interval_from = confidence_interval_from;
        }

        public BigDecimal getConfidence_interval_to() {
            return confidence_interval_to;
        }

        public void setConfidence_interval_to(BigDecimal confidence_interval_to) {
            this.confidence_interval_to = confidence_interval_to;
        }

        public BigDecimal getRelativeRisk_A2B() {
            return relativeRisk_A2B;
        }

        public void setRelativeRisk_A2B(BigDecimal relativeRisk_A2B) {
            this.relativeRisk_A2B = relativeRisk_A2B;
        }

        public BigDecimal getP() {
            return p;
        }

        public void setP(BigDecimal p) {
            this.p = p;
        }

        @Override
        public String toString() {
//            return "{" +
//                    "relativeRisk_A2B=" + relativeRisk_A2B +
//                    ", p=" + p +
//                    ", confidence_interval_from=" + confidence_interval_from +
//                    ", confidence_interval_to=" + confidence_interval_to +
//                    '}';
            return  relativeRisk_A2B +
                    "," + p +
                    "," + confidence_interval_from +
                    "," + confidence_interval_to ;
        }
    }
    //Compare two groups' survival curve
    public static SurvivalAnalysisResult logRankTest(List<BigDecimal> groupA, List<BigDecimal> groupB) {
        final String GROUPA ="A";
        final String GROUPB ="B";
        final int SCALE=8;


        class Entry{

            public Entry(String group, BigDecimal value, int atRiskA, int atRiskB){
                this.group = group;
                this.survivalTime = value;
                this.atRiskTotal = atRiskA + atRiskB;
                this.atRiskA = atRiskA;
                this.atRiskB = atRiskB;
            }

            private BigDecimal survivalTime; // tranformed data, round to nearly 0.05
            private int atRiskTotal;
            private String group;
            private int atRiskA;
            private int atRiskB;
            private BigDecimal expectedA;
            private BigDecimal expectedB;

            public BigDecimal getSurvivalTime() {
                return survivalTime;
            }

            public void setSurvivalTime(BigDecimal survivalTime) {
                this.survivalTime = survivalTime;
            }

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public int getAtRiskA() {
                return atRiskA;
            }

            public void setAtRiskA(int atRiskA) {
                this.atRiskA = atRiskA;
            }

            public int getAtRiskB() {
                return atRiskB;
            }

            public void setAtRiskB(int atRiskB) {
                this.atRiskB = atRiskB;
            }

            public BigDecimal getExpectedA() {
                return expectedA;
            }

            public void setExpectedA(BigDecimal expectedA) {
                this.expectedA = expectedA;
            }

            public BigDecimal getExpectedB() {
                return expectedB;
            }

            public void setExpectedB(BigDecimal expectedB) {
                this.expectedB = expectedB;
            }

            public int getAtRiskTotal() {
                return atRiskTotal;
            }

            public void setAtRiskTotal(int atRiskTotal) {
                this.atRiskTotal = atRiskTotal;
            }
        }


        int atRiskA = groupA.size();
        int atRiskB = groupB.size();
        int observedA = atRiskA;
        int observedB = atRiskB;
        BigDecimal totalExpectedA = BigDecimal.ZERO;
        BigDecimal totalExpectedB = BigDecimal.ZERO;
        List<Entry> records= new ArrayList<Entry>();
        for(BigDecimal value: groupA){
            records.add(new Entry(GROUPA,value,atRiskA,atRiskB));
        }
        for(BigDecimal value: groupB){
            records.add(new Entry(GROUPB,value,atRiskA,atRiskB));
        }



        Collections.sort(records, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return o1.getSurvivalTime().compareTo(o2.getSurvivalTime());
            }
        });

        for(int i=0; i < records.size();i++){
            Entry entry = records.get(i);




            BigDecimal p  = BigDecimal.valueOf(1.0).divide(BigDecimal.valueOf(entry.atRiskTotal),SCALE,BigDecimal.ROUND_UP);
            entry.expectedA = p.multiply(BigDecimal.valueOf(entry.atRiskA));
            entry.expectedB = p.multiply(BigDecimal.valueOf(entry.atRiskB));

            totalExpectedA = totalExpectedA.add(entry.getExpectedA());
            totalExpectedB = totalExpectedB.add(entry.getExpectedB());

//            System.out.println("Group::" + entry.getGroup()+"::SurvivalTime::"+entry.getSurvivalTime()
//                    +"::p::" +p
//                    +"::atRiskA::"+entry.getAtRiskA()+"::atRiskB::"+entry.getAtRiskB()
//                    +"::expectedA::"+ entry.getExpectedA()+"::expectedB::"+ entry.getExpectedB());

            if(i< records.size() -1 ) {
                Entry nextEntry = records.get(i+1);

                if (GROUPA.equals(entry.getGroup())) { //Group A
                    nextEntry.setAtRiskA(entry.getAtRiskA() -1 );
                    nextEntry.setAtRiskB(entry.getAtRiskB());
                } else {
                    nextEntry.setAtRiskB(entry.getAtRiskB() -1 );
                    nextEntry.setAtRiskA(entry.getAtRiskA());
                }
                nextEntry.setAtRiskTotal(nextEntry.getAtRiskA() + nextEntry.getAtRiskB());
            }

        }

//        System.out.println("totalExpectedA::"+totalExpectedA+"observedA::"+observedA);
//        System.out.println("totalExpectedB::"+totalExpectedB+"observedB::"+observedB);

        BigDecimal alpha= totalExpectedA.subtract(new BigDecimal(observedA)).pow(2).divide(totalExpectedA,SCALE,BigDecimal.ROUND_UP);
        alpha = alpha.add(totalExpectedB.subtract(BigDecimal.valueOf(observedB)).pow(2).divide(totalExpectedB,SCALE,BigDecimal.ROUND_UP));




        ChiSquaredDistribution x2 = new ChiSquaredDistribution( 1);
        double p = 1.0 - x2.cumulativeProbability(alpha.doubleValue());
//        System.out.println("ChiSquare is "+ alpha.toString() + "  ::p is "+ p);


        BigDecimal relativeRiskA = BigDecimal.valueOf(observedA).divide(totalExpectedA,SCALE,BigDecimal.ROUND_UP);
        BigDecimal relativeRiskB = BigDecimal.valueOf(observedB).divide(totalExpectedB,SCALE,BigDecimal.ROUND_UP);
        BigDecimal relativeRisk = relativeRiskA.divide(relativeRiskB,SCALE,BigDecimal.ROUND_UP);


//        System.out.println("Relatve Risk is "+relativeRisk);


//        relativeRisk = BigDecimal.valueOf(0.78);
//        totalExpectedA = BigDecimal.valueOf(11.3745);
//        totalExpectedB = BigDecimal.valueOf(10.6255);

        double log_relativeRisk = Math.log(relativeRisk.doubleValue());

        double se_log_relativeRisk =Math.sqrt(BigDecimal.valueOf(1.0).divide(totalExpectedA,SCALE, BigDecimal.ROUND_UP).add(
                BigDecimal.valueOf(1.0).divide(totalExpectedB,SCALE, BigDecimal.ROUND_UP)
        ).doubleValue());


        double log_relativeRisk_2se_from  = log_relativeRisk - 2 * se_log_relativeRisk;

        double log_relativeRisk_2se_to  = log_relativeRisk + 2 * se_log_relativeRisk;

        double relativeRisk_2se_from = Math.exp(log_relativeRisk_2se_from);

        double relativeRisk_2se_to = Math.exp(log_relativeRisk_2se_to);

//        System.out.println("log_relativeRisk::"+log_relativeRisk+
//                "Standard Error of Relatve Risk is "+se_log_relativeRisk +
//                log_relativeRisk_2se_from + " -> "+log_relativeRisk_2se_to +"::"+
//                relativeRisk_2se_from +" ->"+ relativeRisk_2se_to);


        SurvivalAnalysisResult result = new SurvivalAnalysisResult();
        result.setConfidence_interval_from(BigDecimal.valueOf(relativeRisk_2se_from));
        result.setConfidence_interval_to(BigDecimal.valueOf(relativeRisk_2se_to));
        result.setRelativeRisk_A2B(relativeRisk);
        result.setP(BigDecimal.valueOf(p));
        result.setCountA(groupA.size());
        result.setCountB(groupB.size());
        return result;
    }





}
