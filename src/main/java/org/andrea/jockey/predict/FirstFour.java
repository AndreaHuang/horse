package org.andrea.jockey.predict;

import org.andrea.jockey.app.JockeyApp;
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

    private static final int MAX_WINODDS=70;
    private static final BigDecimal ODD_A=new BigDecimal(5);
    private static final BigDecimal ODD_B=new BigDecimal(20);




    private List<String> decideBets(List<RaceCardResult> resultList,FirstFourStrategy strategy){

        if(strategy.isThreeParts()){
            return decideBets_threeParts(resultList,strategy);
        }
        else if(strategy.isTwoParts()){
           // return decideBets_twoParts(resultList,strategy);
            return decideBets_twoParts_WithFixed(resultList,strategy);
        } else {
            return decideBets_onePart(resultList,strategy);
        }
    }

    private List<String> decideBets_onePart(List<RaceCardResult> resultList, FirstFourStrategy strategy){
        /* decide the Bet*/
        List<String> selected_horseNums = new ArrayList<>();
        List<RaceCardResult> horses = selectHorse(resultList,strategy.getSeq1());
        for(RaceCardResult aRaceCard: horses){
            selected_horseNums.add(aRaceCard.getHorseNo());
        }

        List<String> bets = iterateTheBets(selected_horseNums);
        return bets;
    }

    private List<String> decideBets_twoParts(List<RaceCardResult> resultList, FirstFourStrategy strategy){
        /* decide the Bet*/
        List<String> selected_horseNums_part1 = new ArrayList<>();
        List<String> selected_horseNums_part2 = new ArrayList<>();
        List<RaceCardResult> horses_part1 = selectHorse(resultList,strategy.getSeq1());
        for(RaceCardResult aRaceCard: horses_part1){
            selected_horseNums_part1.add(aRaceCard.getHorseNo());
        }

        List<RaceCardResult> horhorses_part2 = selectHorse(resultList,strategy.getSeq2());
        for(RaceCardResult aRaceCard: horhorses_part2){
            selected_horseNums_part2.add(aRaceCard.getHorseNo());
        }

        List<String> bets = iterateTheBets(selected_horseNums_part1,strategy.getCntFromSeq1(),
                selected_horseNums_part2,4-strategy.getCntFromSeq1());


        return bets;
    }

    private List<String> decideBets_twoParts_WithFixed(List<RaceCardResult> resultList, FirstFourStrategy strategy){
        /* decide the Bet*/
        List<String> selected_horseNums_part1 = new ArrayList<>();
        List<String> selected_horseNums_part2 = new ArrayList<>();

        boolean hasFixed =false;
        if(strategy.getFixed()!=null) {
            hasFixed=true;
        }
        List<RaceCardResult> horses_part1 = selectHorse(resultList,strategy.getSeq1());
        for(RaceCardResult aRaceCard: horses_part1){
            selected_horseNums_part1.add(aRaceCard.getHorseNo());
        }

        List<RaceCardResult> horhorses_part2 = selectHorse(resultList,strategy.getSeq2());
        for(RaceCardResult aRaceCard: horhorses_part2){
            selected_horseNums_part2.add(aRaceCard.getHorseNo());
        }
        System.out.println("selected_horseNums_part1 "+ selected_horseNums_part1.toString());
        System.out.println("selected_horseNums_part2 "+ selected_horseNums_part2.toString());

        List<String> bets = iterateTheBets(selected_horseNums_part1,strategy.getCntFromSeq1(),
                selected_horseNums_part2,hasFixed ? 3-strategy.getCntFromSeq1():4-strategy.getCntFromSeq1() );
        int i=0;
        List<String> result = new ArrayList<>();
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        };
        if(strategy.getFixed()!=null) {
            for (int aFixed : strategy.getFixed()) {
                String addAHorseNo = resultList.get(aFixed).getHorseNo();
                for (String bet : bets) {
                    List<String> combined = (Arrays.asList(bet.split(",")));

                    List<String> allHorse = new ArrayList<>();
                    allHorse.addAll(combined);
                    allHorse.add(addAHorseNo);

                    Collections.sort(allHorse, comparator);

                    String thisResult = String.join(",", allHorse);

                    result.add(thisResult);
                }
            }
            for (String bet : result) {
                System.out.println("bet " + i++ + ": " + bet);
            }
            return result;

        }

        return bets;
    }

    private List<String> decideBets_threeParts(List<RaceCardResult> resultList, FirstFourStrategy strategy){
        /* decide the Bet*/
        List<String> selected_horseNums_part1 = new ArrayList<>();
        List<String> selected_horseNums_part2 = new ArrayList<>();
        List<String> selected_horseNums_part3 = new ArrayList<>();

        boolean hasFixed =false;
        if(strategy.getFixed()!=null) {
            hasFixed=true;
        }
        List<RaceCardResult> horses_part1 = selectHorse(resultList,strategy.getSeq1());
        for(RaceCardResult aRaceCard: horses_part1){
            selected_horseNums_part1.add(aRaceCard.getHorseNo());
        }

        List<RaceCardResult> horhorses_part2 = selectHorse(resultList,strategy.getSeq2());
        for(RaceCardResult aRaceCard: horhorses_part2){
            selected_horseNums_part2.add(aRaceCard.getHorseNo());
        }

        List<RaceCardResult> horhorses_part3 = selectHorse(resultList,strategy.getSeq3());
        for(RaceCardResult aRaceCard: horhorses_part3){
            selected_horseNums_part3.add(aRaceCard.getHorseNo());
        }
        System.out.println("selected_horseNums_part1 "+ selected_horseNums_part1.toString());
        System.out.println("selected_horseNums_part2 "+ selected_horseNums_part2.toString());
        System.out.println("selected_horseNums_part3 "+ selected_horseNums_part3.toString());

        List<String> bets = iterateTheBets(selected_horseNums_part1,strategy.getCntFromSeq1(),
                selected_horseNums_part2,strategy.getCntFromSeq2(),
                selected_horseNums_part3, 4 - strategy.getCntFromSeq1() - strategy.getCntFromSeq2());


        List<String> result = new ArrayList<>();
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        };

        int i=0;
        for (String bet : result) {
            System.out.println("bet " + i++ + ": " + bet);
        }
        return bets;
    }

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

    private List<String> iterateTheBets(List<String> dataList1,int numFromList1, List<String> dataList2,int numFromList2){

        String[] resultList1 = new String[numFromList1];
        List<String> allResult1= new ArrayList<>();
        System.out.println(String.format("C(%d, %d) = %d", dataList1.size(), numFromList1, combination(dataList1.size(), numFromList1)));
        combinationSelect(dataList1.toArray(new String[dataList1.size()]), 0, resultList1, 0,allResult1);


        String[] resultList2 = new String[numFromList2];
        List<String> allResult2= new ArrayList<>();
        System.out.println(String.format("C(%d, %d) = %d", dataList2.size(), numFromList2, combination(dataList2.size(), numFromList2)));
        combinationSelect(dataList2.toArray(new String[dataList2.size()]), 0, resultList2, 0,allResult2);

        int i=1;
//        for(String fromList1: allResult1){
//            System.out.println("From Result 1 : "+ i++ +": "+fromList1);
//        }
//        for(String fromList2: allResult2){
//            System.out.println("From Result 2 : "+ i++ +": "+fromList2);
//        }

        List<String> allResult= new ArrayList<>();

        for(String fromList1: allResult1){
//            System.out.println("fromList1: "+ fromList1);
            List<String> combinedResult_part1 = new ArrayList<>();
            String[] arr_part1 =  fromList1.split(",");
            for(String a: arr_part1){
                combinedResult_part1.add(a);
            }

            for(String fromList2: allResult2){
//                System.out.println("fromList2: "+ fromList2);
                List<String> combinedResult_part2 = new ArrayList<>();
                String[] arr_part2 =  fromList2.split(",");
                for(String a: arr_part2){
                    combinedResult_part2.add(a);
                }

                List<String> combinedResult = new ArrayList<>();
                combinedResult.addAll(combinedResult_part1);
                combinedResult.addAll(combinedResult_part2);

                Collections.sort(combinedResult, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return Integer.parseInt(o1) -Integer.parseInt(o2) ;
                    }
                });

                String thisResult = String.join(",", combinedResult);
                allResult.add(thisResult);

            }
        }

        return allResult;
    }

    private List<String> iterateTheBets(List<String> dataList1,int numFromList1, List<String> dataList2,int numFromList2,
                                        List<String> dataList3,int numFromList3){

        String[] resultList1 = new String[numFromList1];
        List<String> allResult1= new ArrayList<>();
        System.out.println(String.format("C(%d, %d) = %d", dataList1.size(), numFromList1, combination(dataList1.size(), numFromList1)));
        combinationSelect(dataList1.toArray(new String[dataList1.size()]), 0, resultList1, 0,allResult1);


        String[] resultList2 = new String[numFromList2];
        List<String> allResult2= new ArrayList<>();
        System.out.println(String.format("C(%d, %d) = %d", dataList2.size(), numFromList2, combination(dataList2.size(), numFromList2)));
        combinationSelect(dataList2.toArray(new String[dataList2.size()]), 0, resultList2, 0,allResult2);

        String[] resultList3 = new String[numFromList3];
        List<String> allResult3= new ArrayList<>();
        System.out.println(String.format("C(%d, %d) = %d", dataList2.size(), numFromList3, combination(dataList3.size(), numFromList3)));
        combinationSelect(dataList3.toArray(new String[dataList3.size()]), 0, resultList3, 0,allResult3);


        int i=1;
//        for(String fromList1: allResult1){
//            System.out.println("From Result 1 : "+ i++ +": "+fromList1);
//        }
//        for(String fromList2: allResult2){
//            System.out.println("From Result 2 : "+ i++ +": "+fromList2);
//        }

        List<String> allResult= new ArrayList<>();

        for(String fromList1: allResult1){
//            System.out.println("fromList1: "+ fromList1);
            List<String> combinedResult_part1 = new ArrayList<>();
            String[] arr_part1 =  fromList1.split(",");
            for(String a: arr_part1){
                combinedResult_part1.add(a);
            }

            for(String fromList2: allResult2){
//                System.out.println("fromList2: "+ fromList2);
                List<String> combinedResult_part2 = new ArrayList<>();
                String[] arr_part2 =  fromList2.split(",");
                for(String a: arr_part2){
                    combinedResult_part2.add(a);
                }

                for(String fromList3: allResult3){
                    List<String> combinedResult_part3 = new ArrayList<>();
                    String[] arr_part3 =  fromList3.split(",");

                    for(String a: arr_part3){
                        combinedResult_part3.add(a);
                    }

                    List<String> combinedResult = new ArrayList<>();
                    combinedResult.addAll(combinedResult_part1);
                    combinedResult.addAll(combinedResult_part2);
                    combinedResult.addAll(combinedResult_part3);

                    Collections.sort(combinedResult, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return Integer.parseInt(o1) -Integer.parseInt(o2) ;
                        }
                    });

                    String thisResult = String.join(",", combinedResult);
                    allResult.add(thisResult);

                }
            }
        }

        return allResult;
    }

    private List<RaceCardResult> selectHorse(List<RaceCardResult> raceCardResults, int[] indList ){
        List<RaceCardResult> dataList = new ArrayList<>();
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
    public void calGains(String fromDate, String toDate,String fileName, String place, int distance, int raceClass, String course){

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
                allResult.addAll(calGains(raceDate,place,distance,raceClass,course));
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
    private  List<MetricResult> calGains(String raceDate, String place, int distance, int raceClass, String course) throws IOException {
        List<MetricResult> resultList = new ArrayList<>();
        System.out.println("checking "+raceDate);
        int raceNumber = dao.getRaceSeqOfDay(raceDate);
        for (int i = 1; i <= raceNumber; i++) {
            List<MetricResult> aRace= this.calGains(raceDate, i, place, distance, raceClass, course);
            if(aRace!=null){
                resultList.addAll(aRace);
            }
        }
        return resultList;
    }

    private List<MetricResult> calGains(String raceDate, int seqOfDay, String place,int distance, int raceClass, String course) throws IOException {

        System.out.println(raceDate+":"+seqOfDay);
        /* Get the Race Card Result*/
        String sql = "select * from racecard " +
                "where racedate = "+ raceDate +" and raceSeqOfDay =" +seqOfDay ;

                if(place!=null && !place.isEmpty()){
                    sql =sql +" and raceMeeting = '"+place+"'";
                }
                if(distance > 0){
                    sql = sql + " and distance= "+ distance;
                }
                if(raceClass > 0){
                    sql = sql + " and raceclass= "+ raceClass;
                }
                if(course !=null) {
                    sql = sql + " and upper(course)= '"+course+"'";
                }
                sql = sql + " and winOdds <= " +   MAX_WINODDS ;
               sql = sql + " order by winOdds asc";
        List<RaceCardResult> resultList = dao.queryRaceResult(sql);

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

        int totalNum = resultList.size();
        String joined_horseNums = String.join("-",horseNums);
        String joined_odds = String.join("-",odds);
        List<String> oddsSections_all = new ArrayList<>();
        for(String aOdd: odds){
            BigDecimal odd= new BigDecimal(aOdd);
            if(ODD_A.compareTo(odd) >=0){
                oddsSections_all.add("A");
            } else if(ODD_B.compareTo(odd)>=0){
                oddsSections_all.add("B");
            } else {
                oddsSections_all.add("C");
            }
        }
        Collections.sort(oddsSections_all);
        String joined_oddsSections_all = String.join("",oddsSections_all);






        /* Get the dividend*/
        List<Dividend> dividends=dao.queryDividend("Select * from dividend where raceDate = "+ raceDate
            +" and raceSeqOfDay ="+ seqOfDay +" and pool='FIRST 4'");

        BigDecimal first4_Dividend =null;
        String first4_horseNum=null;
        String first4_odds = null;
        String first4_odds_seq = null;
        String joined_oddsSections =null;
       for(Dividend div: dividends){
           if("FIRST 4".equals(div.getPool())){
               first4_Dividend=div.getDividend();
               first4_horseNum = div.getWinning();
               /*Get the winning ones' odd */
               String[] first4_horses= first4_horseNum.split(",");
               List<String> first4_horses_odds = new ArrayList<>();
               List<String> first4_horses_oddSeq = new ArrayList<>();

               for(String aHorse : first4_horses){
                   int i =0;
                   for(RaceCardResult aRaceCard: resultList){
                       i++;
                       if(aHorse.equals(aRaceCard.getHorseNo())) { //if is the horse no, get the winodd
                           first4_horses_odds.add(String.valueOf(aRaceCard.getWinOdds()));
                           first4_horses_oddSeq.add(Integer.toString(i));
                       }
                   }
               }
               first4_odds = String.join("-",first4_horses_odds);
               first4_odds_seq=String.join("~",first4_horses_oddSeq);
               List<String> oddsSections = new ArrayList<>();
               for(String aOdd: first4_horses_odds){
                   BigDecimal odd= new BigDecimal(aOdd);
                   if(ODD_A.compareTo(odd) >=0){
                       oddsSections.add("A");
                   } else if(ODD_B.compareTo(odd)>=0){
                       oddsSections.add("B");
                   } else {
                       oddsSections.add("C");
                   }
               }
               Collections.sort(oddsSections);
               joined_oddsSections = String.join("",oddsSections);
               break;
           }
       }
       if(first4_horseNum ==null){
           return null;
       }



        System.out.println("first4_horseNum : " +first4_horseNum);
        StringBuilder sb = new StringBuilder(8);



        List<MetricResult> allResult = new ArrayList<>();
        /* decide the Bet*/

        for(FirstFourStrategy strategy : JockeyApp.allStrategy) {
            List<String> bets = decideBets(resultList,strategy);
            BigDecimal paid=new BigDecimal(10).multiply(new BigDecimal(bets.size()));
            BigDecimal gained =BigDecimal.ZERO;

            Set<String> pickedNumer = new HashSet<>();
            for (String aBet : bets) {
                pickedNumer.addAll(Arrays.asList(aBet.split(",")));
                if (aBet.equals(first4_horseNum)) {
                    gained = first4_Dividend;
                }
            }

            List<String> pickedNumerList = new ArrayList<>(pickedNumer);


            MetricResult result = new MetricResult();
            result.setMethod(strategy.getName());

            result.setTtlCntHorse(totalNum);

            result.setRaceMeeting(resultList.get(0).getRacePlace());
            result.setCourse(resultList.get(0).getCourse());
            result.setRaceDate(raceDate);
            result.setRaceSeq(seqOfDay);
            result.setDistance(resultList.get(0).getDistance());
            result.setRaceClass(resultList.get(0).getRaceClass());
            result.setGoing(resultList.get(0).getGoing());
            result.setHorseNum(joined_horseNums);
            result.setOdds(joined_odds);
            result.setJoined_oddsSections(joined_oddsSections);
            result.setFirst4_Dividend(first4_Dividend);
            result.setFirst4_horseNum(first4_horseNum.replace(",", "-"));
            result.setFirst4_odds(first4_odds);
            result.setFirst4_odds_seq(first4_odds_seq);
            result.setFirst4_Dividend_Gained(gained);
            result.setFirst4_Dividend_Paid(paid);
            result.setJoined_oddsSections_all(joined_oddsSections_all);
            allResult.add(result);
        }
        return allResult;

    }


    private static class MetricResult{
        private String method;
        private String joined_oddsSections;
        private String  joined_oddsSections_all;
        private String raceMeeting;
        private String course;
        private int ttlCntHorse;
          private String raceDate;
          private int raceSeq;
          private int distance;
          private int raceClass;
          private String going;
          private String horseNum;
          private String odds;
          private BigDecimal  first4_Dividend;
          private String  first4_horseNum;
          private String  first4_odds;
          private String  first4_odds_seq;
          private BigDecimal first4_Dividend_Paid = BigDecimal.ZERO;
          private BigDecimal first4_Dividend_Gained = BigDecimal.ZERO;

        public void setJoined_oddsSections_all(String joined_oddsSections_all) {
            this.joined_oddsSections_all = joined_oddsSections_all;
        }

        public void setFirst4_odds_seq(String first4_odds_seq) {
            this.first4_odds_seq = first4_odds_seq;
        }

        public void setTtlCntHorse(int ttlCntHorse) {
            this.ttlCntHorse = ttlCntHorse;
        }

        public void setOdds(String odds) {
            this.odds = odds;
        }

        static String printHeader(){
          String header = "RaceMeeting,RaceDate,SeqOfDay,Distance,Course,Class,Going,HorseNum, Odds,TotalCnt," +
                  "Method,OddsSection_all,OddsSection," +
                  "First4-HorseNum,First4-Odds,First4-OddsSeq," +
                  "First4-Dividend,First4-Pay,First4-Gain";
          return header;
      }
      public String printResult(){


          String result = String.join(",",raceMeeting,raceDate,Integer.toString(raceSeq),
                  Integer.toString(distance),course,Integer.toString(raceClass),going,
                  horseNum,odds,
                  Integer.toString(ttlCntHorse),
                  method,joined_oddsSections_all,joined_oddsSections,
                  first4_horseNum,first4_odds,first4_odds_seq,first4_Dividend.toString(),
                  first4_Dividend_Paid.toString(),first4_Dividend_Gained.toString());
          return result;
      }

        public void setFirst4_odds(String first4_odds) {
            this.first4_odds = first4_odds;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public void setJoined_oddsSections(String joined_oddsSections) {
            this.joined_oddsSections = joined_oddsSections;
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
