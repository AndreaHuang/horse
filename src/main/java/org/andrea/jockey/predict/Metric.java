package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.Dividend;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Metric {

    @Autowired
    RecordCardDAO dao;


    public void checkForcastMetric(String fromDate, String toDate,String fileName){

        FileWriter writer = null;
        File f =null;

        try {

           List<String> racesDates =  dao.getRaceDates(fromDate,toDate);

            f = new File(fileName+".csv");
            writer = new FileWriter(f);
            writer.write("RaceDate,SeqOfDay,Distance,Class,Going,ActualPlace,Bingo,Win-Pay,Win-Gain,Place-Pay," +
                            "Place-Gain,Q-Pay,Q-Gain,QPlace-Pay,QPlace-Gain,TRIO-Pay,TRIO-Gain");
            writer.write("\r\n");


            for(String raceDate: racesDates){

                this.checkForcastMetric(writer, raceDate);

            }
            writer.close();
            writer= null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = null;
            }
        }
    }
    private void checkForcastMetric(FileWriter writer, String raceDate) throws IOException {
        System.out.println("checking "+raceDate);
        int raceNumber = dao.getRaceSeqOfDay(raceDate);
        for (int i = 1; i <= raceNumber; i++) {
            this.statisticRaceCard(writer,raceDate, i);
        }
    }

    private void statisticRaceCard(FileWriter writer,String raceDate, int seqOfDay) throws IOException {
        System.out.println(raceDate+":"+seqOfDay);
//        if(
//                //raceDate.equals("20171011")||
//                raceDate.equals("20171126")|| raceDate.equals("20171223")
//           ||raceDate.equals("20180128")
//            ||raceDate.equals("20180221") ||
//                raceDate.equals("20180318")
//        || raceDate.equals("20180328")
//                || raceDate.equals("20180429")
//        ||raceDate.equals("20180506")
//        ||raceDate.equals("20180527")
//                ||raceDate.equals("20180603")
//                ||raceDate.equals("20180613")
//                ||raceDate.equals("20180616")
//                ||raceDate.equals("20180624")
//                ||raceDate.equals("20180909")
//                ||raceDate.equals("20181028")
//                ||raceDate.equals("20181114")
//                ||raceDate.equals("20181118")
//                ||raceDate.equals("20181125")
//                ||raceDate.equals("20181216")
//                ||raceDate.equals("20190106")
//                ||raceDate.equals("20190116")
//                ||raceDate.equals("20190120")
//
//
//        )
//
//    {
//            return;
//        }
        List<RaceCardResult> resultList = dao.queryRaceResult("select * from racecard " +
                "where racedate = "+ raceDate +" and raceSeqOfDay =" +seqOfDay +
                " and predicted_place is not null "+
                " order by predicted_place asc");

        if(resultList.isEmpty()){
            System.err.println("Empty Result:"+ raceDate +":"+seqOfDay);
            return;
        }

        List<Dividend> dividends=dao.queryDividend("Select * from dividend where raceDate = "+ raceDate
            +" and raceSeqOfDay ="+ seqOfDay);

        BigDecimal win_Dividend =null;
        BigDecimal trio_Dividend=null;
        Map<String,BigDecimal> place_Dividend=new HashMap<>();
        Map<String,BigDecimal> quinella_Dividend=new HashMap<>();
        Map<String,BigDecimal> quinella_place_Dividend=new HashMap<>();
        String trio_winning=null;
       for(Dividend div: dividends){
           if("WIN".equals(div.getPool())){
               win_Dividend=div.getDividend();
           } else if("PLACE".equals(div.getPool())){
               place_Dividend.put(div.getWinning(),div.getDividend());
           } else if("QUINELLA".equals(div.getPool())){
               quinella_Dividend.put(div.getWinning(),div.getDividend());
           } else if("QUINELLA PLACE".equals(div.getPool())){
               quinella_place_Dividend.put(div.getWinning(),div.getDividend());
           } else if("TRIO".equals(div.getPool())){
               trio_Dividend = div.getDividend();
               trio_winning =div.getWinning();
           }
       }


        StringBuilder sb = new StringBuilder(8);

        int i=0;
        int bingo=0;
        List<String> horseNo_PredictedPlace=new ArrayList<>();
        List<String> horseNo_PredictedQ = new ArrayList<>();

        BigDecimal quinella_Dividend_Gained = BigDecimal.ZERO;
        BigDecimal win_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal place_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal quinella_place_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal trio_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal trio_Dividend_Paid=BigDecimal.ZERO;

        for(RaceCardResult aItem : resultList){
            if(aItem.getPlace()==1 && aItem.getPredicted_place() ==1) {
                win_Dividend_Gained = win_Dividend;
            }
            if(aItem.getPredicted_place()>0 && aItem.getPredicted_place()<=3 && aItem.getPlace()<=3){
                if(place_Dividend.get(aItem.getHorseNo())!=null){
                    place_Dividend_Gained=place_Dividend_Gained.add(place_Dividend.get(aItem.getHorseNo()));
                    bingo++;
                }

            }
            if(aItem.getPredicted_place()>0 && aItem.getPredicted_place()<=3){
                horseNo_PredictedPlace.add(aItem.getHorseNo());
            }
            if(aItem.getPredicted_place()>0 && aItem.getPredicted_place()<=2){
                horseNo_PredictedQ.add(aItem.getHorseNo());
            }

            //Calculate
            sb.append(aItem.getPlace()).append("-");
        }
        Collections.sort(horseNo_PredictedPlace, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        });
        Collections.sort(horseNo_PredictedQ, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        });
        if(horseNo_PredictedPlace.size()==3) {
            String comb1 = horseNo_PredictedPlace.get(0) + "," + horseNo_PredictedPlace.get(1);
            String comb2 = horseNo_PredictedPlace.get(1) + "," + horseNo_PredictedPlace.get(2);
            String comb3 = horseNo_PredictedPlace.get(0) + "," + horseNo_PredictedPlace.get(2);

            quinella_place_Dividend_Gained = quinella_place_Dividend_Gained.add(
                    quinella_place_Dividend.containsKey(comb1) ?
                            quinella_place_Dividend.get(comb1) : BigDecimal.ZERO);

            quinella_place_Dividend_Gained = quinella_place_Dividend_Gained.add(
                    quinella_place_Dividend.containsKey(comb2) ?
                            quinella_place_Dividend.get(comb2) : BigDecimal.ZERO);

            quinella_place_Dividend_Gained = quinella_place_Dividend_Gained.add(
                    quinella_place_Dividend.containsKey(comb3) ?
                            quinella_place_Dividend.get(comb3) : BigDecimal.ZERO);

            if(trio_Dividend!=null && trio_Dividend.compareTo(BigDecimal.valueOf(29))>0){
                trio_Dividend_Paid=BigDecimal.valueOf(10);
                String trio = horseNo_PredictedPlace.get(0) + "," + horseNo_PredictedPlace.get(1)+","+ horseNo_PredictedPlace.get(2);
                if(trio_winning.equals(trio)){
                    trio_Dividend_Gained = trio_Dividend;
                }
            }

        }else  if(horseNo_PredictedPlace.size()==2) {
            String comb1 = horseNo_PredictedPlace.get(0) + "," + horseNo_PredictedPlace.get(1);
            quinella_place_Dividend_Gained = quinella_place_Dividend_Gained.add(
                    quinella_place_Dividend.containsKey(comb1) ?
                            quinella_place_Dividend.get(comb1) : BigDecimal.ZERO);
        }
        quinella_Dividend_Gained=quinella_Dividend.containsKey(horseNo_PredictedQ.get(0)+","+horseNo_PredictedQ.get(1))?
                quinella_Dividend.get(horseNo_PredictedQ.get(0)+","+horseNo_PredictedQ.get(1)):BigDecimal.ZERO;

        writer.write(raceDate +","+seqOfDay+","+resultList.get(0).getDistance()+","+
                resultList.get(0).getRaceClass()+","+resultList.get(0).getGoing()+","
                +sb.toString()+","+bingo+",30,"+win_Dividend_Gained+",30,"+place_Dividend_Gained+",30,"+
                quinella_Dividend_Gained +",30,"
                +quinella_place_Dividend_Gained+","+trio_Dividend_Paid+","+trio_Dividend_Gained);
        writer.write("\r\n");


    }


}
