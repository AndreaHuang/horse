package org.andrea.jockey.statistics;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Statistics {

    private static int TARGET_PLACE=3;
    private static SimpleDateFormat DATEFORMATE = new SimpleDateFormat("yyyyMMdd");
    private static Map<Integer,Double> FINISHTIME_SCALE;
    private static Map<String,RaceStatistics>  RACE_STATISTICS;
    @PostConstruct
    public void init(){
        RACE_STATISTICS =this.queryStatistic_Race();
        FINISHTIME_SCALE = createFinishTimeScaleMap();
    }
    @Autowired
    RecordCardDAO dao;
    private static Map<Integer, Double> createFinishTimeScaleMap() {
        Map<Integer,Double> myMap = new HashMap<Integer,Double>();
        myMap.put(1000, 0.2); //57s
        myMap.put(1200, 0.2); //69s
        myMap.put(1400, 0.2); //82s
        myMap.put(1600, 0.2); //94s
        myMap.put(1650, 0.2); //99s
        myMap.put(1800, 0.2); //108s
        myMap.put(2000, 0.2); //121s
        myMap.put(2200, 0.2); //137s
        myMap.put(2400, 0.2); //146s
        return myMap;
    }
    public void buildStatistics_Race(){
        String sql ="  select raceclass, course,going,distance,avg(finishTime) as avg_FinishTime,\n" +
                "  min(finishTime) as min_FinishTime ,count(*) as count from racecard \n" +
                " where place=1 \n" +
                " group by raceclass, course,distance,going\n" +
                " order by raceclass,distance,course,going";
       List<RaceStatistics> races = dao.queryRaceStatistics(sql);
       dao.batchInsertRaceStates(races);
    }
    private  Map<String,RaceStatistics> queryStatistic_Race(){

        List<RaceStatistics> races = dao.queryRaceStatistics("select * from racestats");
        Map<String,RaceStatistics> result =races.stream().collect(Collectors.toMap(x->x.getRaceClass()+"_"
                        +x.getDistance()+"_"+x.getCourse()+"_"+x.getGoing(),
                x->x));
        return result;

    }

    private RaceCardItem buildStatistics_Horse(RaceCardItem r){
        String sql = "select raceclass, place, raceDate,distance,course,going,finishTime " +
                " from racecard where horseId='"+r.getHorseId()+"' and " +
                " raceDate <'"+r.getRaceDate()+"' order by raceDate desc";

        List<HorseStatistics> horses= dao.queryHorseStatistics(sql);

        int totalRaceCount = horses.size();
        if(totalRaceCount == 0){
            r.setHorse_winPer(0.0);
            r.setHorse_winCount(0);
            r.setHorse_newDistance(1);
            r.setHorse_newHorse(1);
            r.setDays_from_lastRace(0);
        } else {
            HorseStatistics lastRace= horses.get(0);
            String thisRaceDate = r.getRaceDate();
            String lastRaceDate = lastRace.getRaceDate();
            int days = this.daysDifference(thisRaceDate,lastRaceDate);
            r.setDays_from_lastRace(days);


            r.setHorse_newHorse(0);
            long inTargetPlace_RaceCount = horses.stream().filter(a -> a.getPlace() <= TARGET_PLACE).count();
            double lifeWin_Percentage = inTargetPlace_RaceCount * 1.0
                    / totalRaceCount;

            lifeWin_Percentage = new BigDecimal(lifeWin_Percentage).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            r.setHorse_winPer(lifeWin_Percentage);
            r.setHorse_winCount(Long.valueOf(inTargetPlace_RaceCount).intValue());

            //New Distance for the horse
            int distanceNotInRangeCount = 0;
            int currentDistance = r.getDistance();
            for (int i = 0; i < Math.min(4,totalRaceCount); i++) {
                HorseStatistics a = horses.get(i);
                if (Math.abs(a.getDistance() - currentDistance) > 250) {
                    distanceNotInRangeCount++;
                }
            }
            if (distanceNotInRangeCount > 3) {
                r.setHorse_newDistance(1);
            } else {
                r.setHorse_newDistance(0);
            }
            //Speed Rating for last 4 races and latest race
            int totalRating=0;
            int latestRating=0;
            for (int i = 0; i < Math.min(4,totalRaceCount); i++) {
                HorseStatistics a = horses.get(i);
                int speedRating = getHorseSpeedRating(a,RACE_STATISTICS);
                if(i==0) {latestRating =speedRating;}
                totalRating =totalRating+speedRating;
            }
            r.setHorse_last4SpeedRate(totalRating/Math.min(4,totalRaceCount));
            r.setHorse_latestSpeedRate(latestRating);
        }


        //Check Jockey
        sql = "select place, raceDate " +
                " from racecard where jockey='"+r.getJockey()+"' and " +
                " raceDate <'"+r.getRaceDate()+"'";

        List<JockeyStatistics> jockeys= dao.queryJockeyStatistics(sql);

        totalRaceCount = jockeys.size();
        if(totalRaceCount == 0){
            r.setJockey_winPer(0.0);
            r.setJockey_winCount(0);
        } else {
            long inTargetPlace_RaceCount = jockeys.stream().filter(a -> a.getPlace() <= TARGET_PLACE).count();
            double jockeyWin_Percentage = inTargetPlace_RaceCount * 1.0
                    / totalRaceCount;

            jockeyWin_Percentage = new BigDecimal(jockeyWin_Percentage).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            r.setJockey_winPer(jockeyWin_Percentage);
            r.setJockey_winCount(Long.valueOf(inTargetPlace_RaceCount).intValue());
        }

        return r;


    }
    private int getHorseSpeedRating(HorseStatistics r,Map<String,RaceStatistics> statisticsMap) {
        String key =String.join("_", Integer.toString(r.getRaceClass()),Integer.toString(r.getDistance()),
                r.getCourse(),r.getGoing());
        RaceStatistics raceStatistics =  statisticsMap.get(key);
        double minFinishTime = raceStatistics.getMinFinishTime();


        int finishTimeScore = 100 - BigDecimal.valueOf(r.getFinishTime()).subtract(BigDecimal.valueOf(minFinishTime)).
                divide(BigDecimal.valueOf(FINISHTIME_SCALE.get(r.getDistance()))).intValue();

        return finishTimeScore;
    }

    /**======================================================= */
        public void check(int raceDate){
        String sql ="select * from newrace where racedate="+raceDate+
                " and raceSeqOfDay=1";

        List<RaceCardResult> results = dao.queryRaceResult(sql);

        for(RaceCardResult aResult: results){
             this.buildStatistics_Horse(aResult);

        }
        results.sort(new Comparator<RaceCardItem>() {
            @Override
            public int compare(RaceCardItem o1, RaceCardItem o2) {
                int result= o1.getRaceSeqOfDay() - o2.getRaceSeqOfDay();
                if(result==0){
                    result = BigDecimal.valueOf(o2.getHorse_winPer()).
                            compareTo(BigDecimal.valueOf(o1.getHorse_winPer()));

                }
                if(result==0){
                    result = BigDecimal.valueOf(o2.getJockey_winPer()).
                            compareTo(BigDecimal.valueOf(o1.getJockey_winPer()));
                }
                return result;
            }
        });
        for(RaceCardResult aResult: results) {
            System.out.println(aResult.printStatisticsResult());
        }
    }
    public void statisticAllNewRace(){
        int raceDate = dao.getNewRaceDate();
        int raceNumber = dao.getNewRaceNumber();
        for(int i=1; i<=raceNumber;i++){
            this.statisticNewRace(raceDate,i);
        }
    }
    public void statisticForPastRecords(int raceDate){

        int raceNumber = dao.getRaceSeqOfDay(raceDate);
        for(int i=1; i<=raceNumber;i++){
            this.statisticRaceCard(raceDate,i);
        }

    }
    private void statisticNewRace(int raceDate,int seq ){

        String sql ="select * from newRace "+
                " where racedate="+raceDate+
                " and raceSeqOfDay="+seq;

        List<RaceCardItem> results = dao.queryNewRace(sql);

        for(RaceCardItem aResult: results){
            this.buildStatistics_Horse(aResult);

        }

        dao.batchUpdateRaceStatistic(results,true);
    }
    public void statisticRaceCard(int raceDate,int seq ){

        String sql ="select * from  raceCard " +
                " where racedate="+raceDate+
                " and raceSeqOfDay="+seq;

        List<RaceCardResult> queryResults = dao.queryRaceResult(sql);

        List<RaceCardItem> statisticResults = new ArrayList<>();
        for(RaceCardItem aResult: queryResults){
            RaceCardItem result =this.buildStatistics_Horse(aResult);
            statisticResults.add(result);




            //System.out.println(result.printStatisticsResult());
        }

        dao.batchUpdateRaceStatistic(statisticResults,false);
    }
    private int daysDifference(String day1, String day2){
       try {
          Date d1 =  DATEFORMATE.parse(day1);
          Date d2 = DATEFORMATE.parse(day2);
          long timeDiff = d1.getTime()-d2.getTime();
          int daysDiff = Math.abs((int)(timeDiff/24/3600/1000));
          return daysDiff;

       } catch(ParseException e){
           e.printStackTrace();
           throw new RuntimeException();
       }
    }
    public void buildStatistics_allRaceCards(String daysOnAfter){
       List<String> allDates = dao.getRaceDates(daysOnAfter);
       for(String aDate: allDates){
           System.out.println("build Statics for race day:"+aDate);
           this.statisticForPastRecords(Integer.valueOf(aDate));
       }
    }
    public void buildStatistics_allRaceCards(String daysFrom, String daysOnAfter){
        List<String> allDates = dao.getRaceDates(daysFrom, daysOnAfter);
        for(String aDate: allDates){
            System.out.println("build Statics for race day:"+aDate);
            this.statisticForPastRecords(Integer.valueOf(aDate));
        }
    }
}
