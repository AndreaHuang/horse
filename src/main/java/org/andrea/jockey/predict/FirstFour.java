package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.Dividend;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirstFour {

    @Autowired
    RecordCardDAO dao;

    private static final int MAX_WINODDS=50;
    private static int[] odds_123459A = new int[]{0,1,2,3,4,8,9};
    private static int[] odds_1234589 = new int[]{0,1,2,3,4,7,8};


    private static final String METHOD="odds_1234589";

    private List<String> iterateTheBets(List<String> dataList){
        Collections.sort(dataList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) -Integer.parseInt(o2) ;
            }
        });
        int n=4;
        String[] resultList = new String[4];
        List<String> allResult= new ArrayList<>();

        System.out.println(String.format("C(%d, %d) = %d", dataList.size(), n, combination(dataList.size(), n)));
        combinationSelect(dataList.toArray(new String[dataList.size()]), 0, resultList, 0,allResult);

        return allResult;
    }


    private List<RaceCardResult> selectHorse(List<RaceCardResult> raceCardResults){
        List<RaceCardResult> dataList = new ArrayList<>();
        int[] indList = odds_1234589;
        for(int i=0;i<indList.length;i++){
            int idx=indList[i];
            if(raceCardResults.size()>idx ) {
               // if (raceCardResults.get(idx).getWinOdds() < MAX_WINODDS) {
                    dataList.add(raceCardResults.get(idx));
                //}
            }

        }
        return dataList;
    }

    private List<RaceCardResult> selectHorse_Lowest7(List<RaceCardResult> raceCardResults){


        List<RaceCardResult> dataList = new ArrayList<>();
        int i=0;

        for(RaceCardResult aHorse: raceCardResults){
            if(i++ < 7) {
                dataList.add(aHorse);
            }
        }

       return dataList;
    }

    private List<RaceCardResult> selectHorse_range2_8(List<RaceCardResult> raceCardResults){


        List<RaceCardResult> dataList = new ArrayList<>();

        for(int i=1;i<8 && i<raceCardResults.size();i++){
            dataList.add(raceCardResults.get(i));
        }

        return dataList;
    }

    private List<RaceCardResult> selectHorse_Lowest_8(List<RaceCardResult> raceCardResults){


        List<RaceCardResult> dataList = new ArrayList<>();

        for(int i=0;i<8 && i<raceCardResults.size();i++){
            dataList.add(raceCardResults.get(i));
        }

        return dataList;
    }
    private List<RaceCardResult> selectHorse_1234589(List<RaceCardResult> raceCardResults){


        List<RaceCardResult> dataList = new ArrayList<>();

        dataList.add(raceCardResults.get(0));
        dataList.add(raceCardResults.get(1));
        dataList.add(raceCardResults.get(2));
        dataList.add(raceCardResults.get(3));
        if(raceCardResults.size()>=5) {
            dataList.add(raceCardResults.get(4));
        }
        if(raceCardResults.size()>=8) {
            dataList.add(raceCardResults.get(7));
        }
        if(raceCardResults.size()>=9) {
            dataList.add(raceCardResults.get(8));
        }

        return dataList;
    }
    private List<RaceCardResult> selectHorse_123459A(List<RaceCardResult> raceCardResults){

        List<RaceCardResult> dataList = new ArrayList<>();
        int[] indList = new int[]{0,1,2,3,4,8,9};
        for(int i=0;i<indList.length;i++){
            int idx=indList[i];
            if(raceCardResults.size()>idx ) {
                if (raceCardResults.get(idx).getWinOdds() < MAX_WINODDS) {
                    dataList.add(raceCardResults.get(idx));
                }
            }

        }

        return dataList;
    }
    private List<RaceCardResult> selectHorse_Lowest_6(List<RaceCardResult> raceCardResults){


        List<RaceCardResult> dataList = new ArrayList<>();

        for(int i=0;i<6 && i<raceCardResults.size();i++){
            dataList.add(raceCardResults.get(i));
        }

        return dataList;
    }


    public static void main (String [] args){
        String[] dataList = new String[]{"1","2","3","4","5"};
        String[] result= new String[4];
        List<String> allResult= new FirstFour().combinationSelect(dataList,result,4);
        for(String aResult : allResult){
            System.out.println("Andrea after: " + aResult);
        }

    }
    public List<String> combinationSelect(String[] dataList, String [] result, int n) {
        System.out.println(String.format("A(%d, %d) = %d", dataList.length, n, combination(dataList.length, n)));
        List<String> allResult = new ArrayList<>();
        combinationSelect(dataList, 0, result, 0,allResult);
        return allResult;
    }

    private void combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex, List<String> result) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
            result.add(String.join(",", resultList));
            return;

        }

        // 递归选择下一个
        for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList[i];
            combinationSelect(dataList, i + 1, resultList, resultIndex + 1,result);
        }
    }

    public static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }

    private static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }
    public void calGains(String fromDate, String toDate,String fileName){

        FileWriter writer = null;
        File f =null;

        try {

           List<String> racesDates =  dao.getRaceDates(fromDate,toDate);
           List<MetricResult> allResult = new ArrayList<>();
            f = new File(fileName+"_"+METHOD+".csv");
            writer = new FileWriter(f);
            writer.write(MetricResult.printHeader());
            writer.write("\r\n");


            for(String raceDate: racesDates){
                allResult.addAll(calGains(raceDate));
            }



            BigDecimal total_paid=BigDecimal.ZERO;
            BigDecimal total_gained=BigDecimal.ZERO;


            for(MetricResult aRace : allResult){
                writer.write(aRace.printResult());
                total_paid=total_paid.add(aRace.getFirst4_Dividend_Paid());
                total_gained=total_gained.add(aRace.getFirst4_Dividend_Gained());
                writer.write("\r\n");
            }

            String result = String.join(",",
                    total_paid.toString(),total_gained.toString(),"\r\n");

            String header = String.join(",",
                    "total Paid","total Gain","\r\n");
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
    private  List<MetricResult> calGains(String raceDate) throws IOException {
        List<MetricResult> resultList = new ArrayList<>();
        System.out.println("checking "+raceDate);
        int raceNumber = dao.getRaceSeqOfDay(raceDate);
        for (int i = 1; i <= raceNumber; i++) {
            MetricResult aRace= this.calGains(raceDate, i);
            if(aRace!=null){
                resultList.add(aRace);
            }
        }
        return resultList;
    }

    private MetricResult calGains(String raceDate, int seqOfDay) throws IOException {

        System.out.println(raceDate+":"+seqOfDay);
        /* Get the Race Card Result*/
        List<RaceCardResult> resultList = dao.queryRaceResult("select * from racecard " +
                "where racedate = "+ raceDate +" and raceSeqOfDay =" +seqOfDay +
                " order by winOdds asc");

        if(resultList.isEmpty()){
            System.err.println("Empty Result:"+ raceDate +":"+seqOfDay);
            return null;
        }


        /* List all Horsenum and bet*/
        List<String> horseNums = new ArrayList<>();
        List<String> odds = new ArrayList<>();
        for(RaceCardResult aRaceCard: resultList){
            horseNums.add(aRaceCard.getHorseNo());
            odds.add(String.valueOf(aRaceCard.getWinOdds()));
        }
        String joined_horseNums = String.join("-",horseNums);
        String joined_odds = String.join("-",odds);



        /* decide the Bet*/
        List<String> selected_horseNums = new ArrayList<>();
        List<RaceCardResult> horses = selectHorse(resultList);
        for(RaceCardResult aRaceCard: horses){
            selected_horseNums.add(aRaceCard.getHorseNo());
        }

        List<String> bets = iterateTheBets(selected_horseNums);

        /* Get the dividend*/
        List<Dividend> dividends=dao.queryDividend("Select * from dividend where raceDate = "+ raceDate
            +" and raceSeqOfDay ="+ seqOfDay +" and pool='FIRST 4'");

        BigDecimal first4_Dividend =null;
        String first4_horseNum=null;
       for(Dividend div: dividends){
           if("FIRST 4".equals(div.getPool())){
               first4_Dividend=div.getDividend();
               first4_horseNum = div.getWinning();
               break;
           }
       }
       if(first4_horseNum ==null){
           return null;
       }

        System.out.println("first4_horseNum : " +first4_horseNum);
        StringBuilder sb = new StringBuilder(8);

        BigDecimal paid=new BigDecimal(10).multiply(new BigDecimal(bets.size()));
        BigDecimal gained =BigDecimal.ZERO;

        for(String aBet: bets){
//            System.out.println("aBet : " +aBet);
            if(aBet.equals(first4_horseNum)){
                gained = first4_Dividend;
            }
        }

        MetricResult result = new MetricResult();
        result.setRaceMeeting(resultList.get(0).getRacePlace());
        result.setCourse(resultList.get(0).getCourse());
        result.setRaceDate(raceDate);
        result.setRaceSeq(seqOfDay);
        result.setDistance(resultList.get(0).getDistance());
        result.setRaceClass(resultList.get(0).getRaceClass());
        result.setGoing(resultList.get(0).getGoing());
        result.setHorseNum(joined_horseNums);
        result.setOdds(joined_odds);
        result.setFirst4_Dividend(first4_Dividend);
        result.setFirst4_horseNum(first4_horseNum.replace(",","-"));

        result.setFirst4_Dividend_Gained(gained);
        result.setFirst4_Dividend_Paid(paid);
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
      private String horseNum;
      private String odds;
      private BigDecimal  first4_Dividend;
      private String  first4_horseNum;
      private BigDecimal first4_Dividend_Paid = BigDecimal.ZERO;
      private BigDecimal first4_Dividend_Gained = BigDecimal.ZERO;


        public void setOdds(String odds) {
            this.odds = odds;
        }

        static String printHeader(){
          String header = "RaceMeeting,RaceDate,SeqOfDay,Distance,Course,Class,Going,HorseNum, Odds,First4-HorseNum," +
                  "First4-Dividend,First4-Pay,First4-Gain";
          return header;
      }
      public String printResult(){


          String result = String.join(",",raceMeeting,raceDate,Integer.toString(raceSeq),
                  Integer.toString(distance),course,Integer.toString(raceClass),going,
                  horseNum,odds,
                  first4_horseNum,first4_Dividend.toString(),
                  first4_Dividend_Paid.toString(),first4_Dividend_Gained.toString());
          return result;
      }



        public void setHorseNum(String horseNum) {
            this.horseNum = horseNum;
        }

        public void setFirst4_Dividend(BigDecimal first4_Dividend) {
            this.first4_Dividend = first4_Dividend;
        }

        public void setFirst4_horseNum(String first4_horseNum) {
            this.first4_horseNum = first4_horseNum;
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

        public BigDecimal getFirst4_Dividend_Gained() {
            return first4_Dividend_Gained;
        }
        public BigDecimal getFirst4_Dividend_Paid() {
            return  first4_Dividend_Paid;
        }


        public void setFirst4_Dividend_Gained(BigDecimal first4_Dividend_Gained) {
            this.first4_Dividend_Gained = first4_Dividend_Gained;
        }
        public void setFirst4_Dividend_Paid(BigDecimal first4_Dividend_Paid) {
            this.first4_Dividend_Paid =first4_Dividend_Paid;
        }


    }


}
