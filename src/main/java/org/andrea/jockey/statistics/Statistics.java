package org.andrea.jockey.statistics;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Statistics {

    private static int TARGET_PLACE=3;
    @Autowired
    RecordCardDAO dao;
    public void buildStatistics_Race(){
        String sql ="  select raceclass, course,going,distance,avg(finishTime) as avgFinishTime,\n" +
                "  min(finishTime) as minFinishTime ,count(*) as count from racecard \n" +
                " where place=1 \n" +
                " group by raceclass, course,distance,going\n" +
                " order by raceclass,distance,course,going";
       List<RaceStatistics> races = dao.queryRaceStatistics(sql);
       dao.batchInsertRaceStates(races);
    }
    private Map<String,RaceStatistics> queryStatistic_Race(){

        List<RaceStatistics> races = dao.queryRaceStatistics("select * from racestats");
        Map<String,RaceStatistics> result =races.stream().collect(Collectors.toMap(x->x.getRaceClass()+"_"
                        +x.getDistance()+"_"+x.getCourse()+"_"+x.getGoing(),
                x->x));
        return result;

    }
    private RaceStatistics queryStatistic_Race(Map<String,RaceStatistics> statisticsMap, RaceCardItem r){
        String key =String.join("_", Integer.toString(r.getRaceClass()),Integer.toString(r.getDistance()),
                r.getCourse(),r.getGoing());
        return statisticsMap.get(key);

    }
    private RaceCardItem buildStatistics_Horse(RaceCardItem r){
        String sql = "select place, raceDate,distance,course " +
                " from racecard where horseId='"+r.getHorseId()+"' and " +
                " raceDate <'"+r.getRaceDate()+"' order by raceDate desc";

        List<HorseStatistics> horses= dao.queryHorseStatistics(sql);

        int totalRaceCount = horses.size();
        if(totalRaceCount == 0){
            r.setHorse_winPer(0.0);
            r.setHorse_winCount(0);
            r.setHorse_newDistance(1);
            r.setHorse_newHorse(1);
        } else {
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
    private void statisticRaceCard(int raceDate,int seq ){

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
}
