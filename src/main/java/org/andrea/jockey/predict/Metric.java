package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.Dividend;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.apache.commons.exec.ExecuteException;
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
           List<MetricResult> allResult = new ArrayList<>();
            f = new File(fileName+".csv");
            writer = new FileWriter(f);
            writer.write(MetricResult.printHeader());
            writer.write("\r\n");


            for(String raceDate: racesDates){
                allResult.addAll(checkForcastMetric(raceDate));
            }
            int bingo_0=0;
            int bingo_1=0;
            int bingo_2=0;
            int bingo_3=0;
            int totalRace = allResult.size();
            BigDecimal quinella_Dividend_Paid = BigDecimal.ZERO;
            BigDecimal quinella_Dividend_Gained = BigDecimal.ZERO;

            BigDecimal win_Dividend_Paid =BigDecimal.ZERO;
            BigDecimal win_Dividend_Gained=BigDecimal.ZERO;

            BigDecimal place_Dividend_Paid=BigDecimal.ZERO;
            BigDecimal place_Dividend_Gained=BigDecimal.ZERO;

            BigDecimal quinella_place_Dividend_Paid=BigDecimal.ZERO;
            BigDecimal quinella_place_Dividend_Gained=BigDecimal.ZERO;

            BigDecimal trio_Dividend_Paid=BigDecimal.ZERO;
            BigDecimal trio_Dividend_Gained=BigDecimal.ZERO;



            for(MetricResult aRace : allResult){
                System.out.println("Print:" + aRace.getRaceDate()+","+aRace.getRaceSeq());
                writer.write(aRace.printResult());
                writer.write("\r\n");
                if(aRace.getBingo()==0) {
                    bingo_0++;
                } else if(aRace.getBingo()==1){
                    bingo_1++;
                } else if(aRace.getBingo()==2){
                    bingo_2++;
                } else if(aRace.getBingo()==3){
                    bingo_3++;
                }
                quinella_Dividend_Paid = quinella_Dividend_Paid.add(aRace.getQuinella_Dividend_Paid());
                quinella_Dividend_Gained= quinella_Dividend_Gained.add(aRace.getQuinella_Dividend_Gained());

                win_Dividend_Paid = win_Dividend_Paid.add(aRace.getWin_Dividend_Paid());
                win_Dividend_Gained = win_Dividend_Gained.add(aRace.getWin_Dividend_Gained());

                place_Dividend_Paid=place_Dividend_Paid.add(aRace.getPlace_Dividend_Paid());
                place_Dividend_Gained=place_Dividend_Gained.add(aRace.getPlace_Dividend_Gained());

                quinella_place_Dividend_Paid =quinella_place_Dividend_Paid.add(aRace.getQuinella_place_Dividend_Paid());
                quinella_place_Dividend_Gained =quinella_place_Dividend_Gained.add(aRace.getQuinella_place_Dividend_Gained());

                trio_Dividend_Paid =trio_Dividend_Paid.add(aRace.getTrio_Dividend_Paid());
                trio_Dividend_Gained =trio_Dividend_Gained.add(aRace.getTrio_Dividend_Gained());

            }

            BigDecimal totalGain = trio_Dividend_Gained.add(quinella_Dividend_Gained).add(place_Dividend_Gained);
            String result = String.join(",",
                     Integer.toString(bingo_0),
                    Integer.toString(bingo_1),
                    Integer.toString(bingo_2),
                    Integer.toString(bingo_3),
                    win_Dividend_Paid.toString(),win_Dividend_Gained.toString(),
                    place_Dividend_Paid.toString(),place_Dividend_Gained.toString(),
                    quinella_Dividend_Paid.toString(),quinella_Dividend_Gained.toString(),
                    quinella_place_Dividend_Paid.toString(),quinella_place_Dividend_Gained.toString(),
                    trio_Dividend_Paid.toString(),trio_Dividend_Gained.toString(),totalGain.toString(),"\r\n");

            String header = String.join(",",
                    "Bingo_0","Bingo_1", "Bingo_2", "Bingo_3",
                    "win_Dividend_Paid","win_Dividend_Gained",
                    "place_Dividend_Paid","place_Dividend_Gained",
                    "quinella_Dividend_Paid","quinella_Dividend_Gained",
                    "quinella_place_Dividend_Paid","quinella_place_Dividend_Gained",
                    "trio_Dividend_Paid","trio_Dividend_Gained","\r\n");
            writer.write(header);
            writer.write(result);
            writer.close();
            writer= null;
        } catch (Exception e) {
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
    private  List<MetricResult> checkForcastMetric(String raceDate) throws IOException {
        List<MetricResult> resultList = new ArrayList<>();
        System.out.println("checking "+raceDate);
        int raceNumber = dao.getRaceSeqOfDay(raceDate);
        for (int i = 1; i <= raceNumber; i++) {
            MetricResult aRace= this.statisticRaceCard(raceDate, i);
            if(aRace!=null){
                resultList.add(aRace);
            }
        }
        return resultList;
    }

    private MetricResult statisticRaceCard(String raceDate, int seqOfDay) throws IOException {
//        if(seqOfDay!=6){
//            return null;
//        }
        System.out.println(raceDate+":"+seqOfDay);

        List<RaceCardResult> resultList = dao.queryRaceResult("select * from racecard " +
                "where racedate = "+ raceDate +" and raceSeqOfDay =" +seqOfDay +
                " and predicted_place is not null "+
                " order by predicted_place asc");

        if(resultList.isEmpty()){
            System.err.println("Empty Result:"+ raceDate +":"+seqOfDay);
            return null;
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

        List<String> horseNo_Predicted_Top5= new ArrayList<>();

        BigDecimal quinella_Dividend_Gained = BigDecimal.ZERO;
        BigDecimal win_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal place_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal quinella_place_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal trio_Dividend_Gained=BigDecimal.ZERO;
        BigDecimal trio_Dividend_Paid=BigDecimal.ZERO;
        BigDecimal win_Dividend_Paid =BigDecimal.ZERO;


        for(RaceCardResult aItem : resultList){
            System.out.println(aItem.getHorseNo() +","+aItem.getPredicted_place() + ","+aItem.getPlace());
            if(aItem.getPredicted_place() > 0 && aItem.getPredicted_place() <3 && aItem.getWinOdds()>5){
                win_Dividend_Paid = win_Dividend_Paid.add(BigDecimal.valueOf(10));
                if(aItem.getPlace()==1) {
                    win_Dividend_Gained = win_Dividend;
                }
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
            if(aItem.getPredicted_place()>0 && aItem.getPredicted_place()<=5){
                horseNo_Predicted_Top5.add(aItem.getHorseNo());
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
        Collections.sort(horseNo_Predicted_Top5, new Comparator<String>() {
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

            if(trio_Dividend!=null ){//&& trio_Dividend.compareTo(BigDecimal.valueOf(29))>0){
                trio_Dividend_Paid=BigDecimal.valueOf(100);
                List<String> trioCombination = trioBination(horseNo_Predicted_Top5);
                for(String trio: trioCombination){
                    if(trio_winning.equals(trio)){
                        trio_Dividend_Gained = trio_Dividend;
                        break;
                    }
                }
            }

        }else  if(horseNo_PredictedPlace.size()==2) {
            String comb1 = horseNo_PredictedPlace.get(0) + "," + horseNo_PredictedPlace.get(1);
            quinella_place_Dividend_Gained = quinella_place_Dividend_Gained.add(
                    quinella_place_Dividend.containsKey(comb1) ?
                            quinella_place_Dividend.get(comb1) : BigDecimal.ZERO);
        }
        //Quinella
        quinella_Dividend_Gained=quinella_Dividend.containsKey(horseNo_PredictedQ.get(0)+","+horseNo_PredictedQ.get(1))?
                quinella_Dividend.get(horseNo_PredictedQ.get(0)+","+horseNo_PredictedQ.get(1)):BigDecimal.ZERO;


        MetricResult result = new MetricResult();
        result.setRaceMeeting(resultList.get(0).getRacePlace());
        result.setCourse(resultList.get(0).getCourse());
        result.setRaceDate(raceDate);
        result.setRaceSeq(seqOfDay);
        result.setDistance(resultList.get(0).getDistance());
        result.setRaceClass(resultList.get(0).getRaceClass());
        result.setGoing(resultList.get(0).getGoing());
        result.setPredicted_place(sb.toString());
        result.setBingo(bingo);
        result.setWin_Dividend_Paid(win_Dividend_Paid);
        result.setWin_Dividend_Gained(win_Dividend_Gained);
        result.setPlace_Dividend_Paid(new BigDecimal(30));
        result.setPlace_Dividend_Gained(place_Dividend_Gained);
        result.setQuinella_Dividend_Paid(new BigDecimal(30));
        result.setQuinella_Dividend_Gained(quinella_Dividend_Gained);
        result.setQuinella_place_Dividend_Paid(new BigDecimal(30));
        result.setQuinella_place_Dividend_Gained(quinella_place_Dividend_Gained);
        result.setTrio_Dividend_Paid(trio_Dividend_Paid);
        result.setTrio_Dividend_Gained(trio_Dividend_Gained);
        return result;

    }

    private List<String> trioBination(List<String> top){
        List<String> result = new ArrayList<>();
        if(top.size()==5){
            result.add(top.get(0)+","+top.get(1)+","+top.get(2));
            result.add(top.get(0)+","+top.get(1)+","+top.get(3));
            result.add(top.get(0)+","+top.get(1)+","+top.get(4));
            result.add(top.get(0)+","+top.get(2)+","+top.get(3));
            result.add(top.get(0)+","+top.get(2)+","+top.get(4));
            result.add(top.get(0)+","+top.get(3)+","+top.get(4));
            result.add(top.get(1)+","+top.get(2)+","+top.get(3));
            result.add(top.get(1)+","+top.get(2)+","+top.get(4));
            result.add(top.get(1)+","+top.get(3)+","+top.get(4));
            result.add(top.get(2)+","+top.get(3)+","+top.get(4));
        }
        return result;

    }

    private static class MetricResult{
        private String raceMeeting;
        private String course;
      private String raceDate;
      private int raceSeq;
      private int distance;
      private int raceClass;
      private String going;
      private String predicted_place;
      private int bingo;
      private BigDecimal win_Dividend_Paid = BigDecimal.ZERO;
      private BigDecimal win_Dividend_Gained = BigDecimal.ZERO;
      private BigDecimal place_Dividend_Paid= BigDecimal.ZERO;
      private BigDecimal place_Dividend_Gained= BigDecimal.ZERO;
      private BigDecimal quinella_Dividend_Paid= BigDecimal.ZERO;
      private BigDecimal quinella_Dividend_Gained= BigDecimal.ZERO;
      private BigDecimal quinella_place_Dividend_Paid= BigDecimal.ZERO;
      private BigDecimal quinella_place_Dividend_Gained= BigDecimal.ZERO;
      private BigDecimal trio_Dividend_Paid= BigDecimal.ZERO;
      private BigDecimal trio_Dividend_Gained= BigDecimal.ZERO;


      static String printHeader(){
          String header = "RaceMeeting,RaceDate,SeqOfDay,Distance,Course,Class,Going,ActualPlace,Bingo,Win-Pay,Win-Gain,Place-Pay," +
                    "Place-Gain,Q-Pay,Q-Gain,QPlace-Pay,QPlace-Gain,TRIO-Pay,TRIO-Gain,Total-Gain";
          return header;
      }
      public String printResult(){
          BigDecimal totalGain = trio_Dividend_Gained.add(quinella_Dividend_Gained).add(place_Dividend_Gained);

          String result = String.join(",",raceMeeting,raceDate,Integer.toString(raceSeq),
                  Integer.toString(distance),course,Integer.toString(raceClass),going,predicted_place,
                  Integer.toString(bingo),win_Dividend_Paid.toString(),win_Dividend_Gained.toString(),
                  place_Dividend_Paid.toString(),place_Dividend_Gained.toString(),
                  quinella_Dividend_Paid.toString(),quinella_Dividend_Gained.toString(),
                  quinella_place_Dividend_Paid.toString(),quinella_place_Dividend_Gained.toString(),
                  trio_Dividend_Paid.toString(),trio_Dividend_Gained.toString(),totalGain.toString());
          return result;
      }

        public String getRaceMeeting() {
            return raceMeeting;
        }

        public void setRaceMeeting(String raceMeeting) {
            this.raceMeeting = raceMeeting;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getRaceDate() {
            return raceDate;
        }

        public void setRaceDate(String raceDate) {
            this.raceDate = raceDate;
        }

        public int getRaceSeq() {
            return raceSeq;
        }

        public void setRaceSeq(int raceSeq) {
            this.raceSeq = raceSeq;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getRaceClass() {
            return raceClass;
        }

        public void setRaceClass(int raceClass) {
            this.raceClass = raceClass;
        }

        public String getGoing() {
            return going;
        }

        public void setGoing(String going) {
            this.going = going;
        }

        public String getPredicted_place() {
            return predicted_place;
        }

        public void setPredicted_place(String predicted_place) {
            this.predicted_place = predicted_place;
        }

        public int getBingo() {
            return bingo;
        }

        public void setBingo(int bingo) {
            this.bingo = bingo;
        }

        public BigDecimal getWin_Dividend_Paid() {
            return win_Dividend_Paid;
        }

        public void setWin_Dividend_Paid(BigDecimal win_Dividend_Paid) {
            if(win_Dividend_Paid ==null){
                System.err.println("win_Dividend_Paid is Null");
                return;
            }
            this.win_Dividend_Paid = win_Dividend_Paid;
        }

        public BigDecimal getWin_Dividend_Gained() {
            return win_Dividend_Gained;
        }

        public void setWin_Dividend_Gained(BigDecimal win_Dividend_Gained) {
            if(win_Dividend_Gained ==null){
                System.err.println("win_Dividend_Gained is Null");
                return;
            }
            this.win_Dividend_Gained = win_Dividend_Gained;
        }

        public BigDecimal getPlace_Dividend_Paid() {
            return place_Dividend_Paid;
        }

        public void setPlace_Dividend_Paid(BigDecimal place_Dividend_Paid) {
            if(place_Dividend_Paid ==null){
                System.err.println("place_Dividend_Paid is Null");
                return;
            }
            this.place_Dividend_Paid = place_Dividend_Paid;
        }

        public BigDecimal getPlace_Dividend_Gained() {
            return place_Dividend_Gained;
        }

        public void setPlace_Dividend_Gained(BigDecimal place_Dividend_Gained) {
            if(place_Dividend_Gained ==null){
                System.err.println("place_Dividend_Gained is Null");
                return;
            }
            this.place_Dividend_Gained = place_Dividend_Gained;
        }

        public BigDecimal getQuinella_Dividend_Paid() {
            return quinella_Dividend_Paid;
        }

        public void setQuinella_Dividend_Paid(BigDecimal quinella_Dividend_Paid) {
            if(quinella_Dividend_Paid ==null){
                System.err.println("quinella_Dividend_Paid is Null");
                return;
            }
            this.quinella_Dividend_Paid = quinella_Dividend_Paid;
        }

        public BigDecimal getQuinella_Dividend_Gained() {
            return quinella_Dividend_Gained;
        }

        public void setQuinella_Dividend_Gained(BigDecimal quinella_Dividend_Gained) {
            if(quinella_Dividend_Gained ==null){
                System.err.println("quinella_Dividend_Gained is Null");
                return;
            }
            this.quinella_Dividend_Gained = quinella_Dividend_Gained;
        }

        public BigDecimal getQuinella_place_Dividend_Paid() {
            return quinella_place_Dividend_Paid;
        }

        public void setQuinella_place_Dividend_Paid(BigDecimal quinella_place_Dividend_Paid) {
            if(quinella_place_Dividend_Paid ==null){
                System.err.println("quinella_place_Dividend_Paid is Null");
                return;
            }
            this.quinella_place_Dividend_Paid = quinella_place_Dividend_Paid;
        }

        public BigDecimal getQuinella_place_Dividend_Gained() {
            return quinella_place_Dividend_Gained;
        }

        public void setQuinella_place_Dividend_Gained(BigDecimal quinella_place_Dividend_Gained) {
            if(quinella_place_Dividend_Gained ==null){
                System.err.println("quinella_place_Dividend_Gained is Null");
                return;
            }
            this.quinella_place_Dividend_Gained = quinella_place_Dividend_Gained;
        }

        public BigDecimal getTrio_Dividend_Paid() {
            return trio_Dividend_Paid;
        }

        public void setTrio_Dividend_Paid(BigDecimal trio_Dividend_Paid) {
            if(trio_Dividend_Paid ==null){
                System.err.println("trio_Dividend_Paid is Null");
                return;
            }
            this.trio_Dividend_Paid = trio_Dividend_Paid;
        }

        public BigDecimal getTrio_Dividend_Gained() {
            return trio_Dividend_Gained;
        }

        public void setTrio_Dividend_Gained(BigDecimal trio_Dividend_Gained) {
            if(trio_Dividend_Gained ==null){
                System.err.println("trio_Dividend_Gained is Null");
                return;
            }
            this.trio_Dividend_Gained = trio_Dividend_Gained;
        }
    }


}
