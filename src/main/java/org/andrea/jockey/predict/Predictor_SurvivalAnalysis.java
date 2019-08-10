package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.RaceCardAnalysis;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.andrea.jockey.model.SurvivalAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Predictor_SurvivalAnalysis {

    @Autowired
    RecordCardDAO dao;


    public void calPredicatedPlace(String fromDate, String toDate) {

        List<String> racesDates = dao.getRaceDates(fromDate, toDate);
        for (String aRaceDate : racesDates) {
            int maxRaceSeq = dao.getRaceSeqOfDay(aRaceDate);
            for (int i = 1; i <= maxRaceSeq; i++) {
                System.out.println("calPredicatedPlace " + aRaceDate +"::" +i);
                this.calPredicatedPlace(aRaceDate, i);
            }
        }

    }
    private void calPredicatedPlace(String raceDate, int seqOfDay) {//throws IOException{
         List<SurvivalAnalysis.SurvivalAnalysis_Result> survivalAnalysis_Result = dao. querySurvivalAnalysis_HistoryRecords(
                 "select * from survival_analysis where racedate ='"+raceDate+"' and" +
                " seq = "+seqOfDay);

         Set<Integer> allHorseNo = survivalAnalysis_Result.stream().map(x -> x.getHorseNoA())
                 .collect(Collectors.toSet());
         allHorseNo.addAll(survivalAnalysis_Result.stream().map(x -> x.getHorseNoB())
                 .collect(Collectors.toSet()));
         List<Integer> allHorseNoList = new ArrayList<>(allHorseNo);

        Map<Integer, Map<Integer,Integer>> analyisResultMap = new HashMap<>();
        for(SurvivalAnalysis.SurvivalAnalysis_Result aResult :survivalAnalysis_Result){
            aResult=swapIfNeeded(aResult);
//            aResult.print();
            int horseNoA = aResult.getHorseNoA();
            int horseNoB = aResult.getHorseNoB();
            Map<Integer,Integer> aMap =null;
            if(analyisResultMap.containsKey(horseNoA)){
                aMap = analyisResultMap.get(horseNoA);
            }else {
                aMap = new HashMap<>();
            }
            aMap.put(horseNoB,aResult.getPreditected_result());
            analyisResultMap.put(horseNoA,aMap);
        }

        Collections.sort(allHorseNoList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                boolean needSwap=false;
                Integer lower = o1;
                Integer upper = o2;
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

        System.out.println("predicted result: "+allHorseNoList);
        int place =0;
        for(Integer horseNo: allHorseNoList){
            dao.runSQL("update racecard set predicted_place = "
                    + ++place +" where  racedate='"+raceDate+"' and raceseqofday="+ seqOfDay +" and horseNo = "+ horseNo);
        }

    }
    private SurvivalAnalysis.SurvivalAnalysis_Result swapIfNeeded(SurvivalAnalysis.SurvivalAnalysis_Result aResult){
        if(aResult==null){
            return null;
        }
        if(aResult.getHorseNoA() - (aResult.getHorseNoB())> 0){
            Integer temp = aResult.getHorseNoB();
            aResult.setHorseNoB(aResult.getHorseNoA());
            aResult.setHorseNoA(temp);
            aResult.setPreditected_result(aResult.getPreditected_result() ==1 ? 2:1);
            aResult.setResult(aResult.getResult() ==1 ? 2:1);
            if(aResult.getPreditected_finishTime()!=null) {
                aResult.setPreditected_finishTime(aResult.getPreditected_finishTime().multiply(BigDecimal.valueOf(-1)));
            }
        }
        System.out.println("horseNoA="+aResult.getHorseNoA()+ ",horseNoB="+aResult.getHorseNoB()
                +",predicted_result="+aResult.getPreditected_result());
        return aResult;

    }


}
