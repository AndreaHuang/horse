package org.andrea.jockey.statistics;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.*;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class Statistic_SurvivalAnalysis {

    private static int TARGET_PLACE=3;
    private static int SCALE=8;
    private static SimpleDateFormat DATEFORMATE = new SimpleDateFormat("yyyyMMdd");
    private static Map<Integer,Double> FINISHTIME_SCALE;
    private static Map<String,RaceStatistics>  RACE_STATISTICS;
    @Autowired
    RecordCardDAO dao;


    public Map<SurvivalAnalysis.SurvivalAnalysis_Core,Map<String,Map<String, StatUtils.SurvivalAnalysisResult>>>
                    testLogRankTest_horse(String sql){

       Map<SurvivalAnalysis.SurvivalAnalysis_Core,Map<String,Map<String, StatUtils.SurvivalAnalysisResult>>> result=
                new HashMap<SurvivalAnalysis.SurvivalAnalysis_Core,Map<String,Map<String, StatUtils.SurvivalAnalysisResult>>>();
//        String sql =
//        "select racedate, horseId,finishTime,distance, racemeeting, upper(course) as course  from racecard "+
//        " where  distance  in ('1400') "+
//        " and horseId in ('B149','A041','B227','A363','B317','C203','A338','V379','B169','C271') "+
//        " and racedate < 20190530 ";

        List<SurvivalAnalysis.SurvivalAnalysis_Horse>  survivalAnalysis_horses= dao.querySurvivalAnalysis_Horse(sql);

        //Split by racemeeting, distance,course,
        //B010
        Map<SurvivalAnalysis.SurvivalAnalysis_Core, List<SurvivalAnalysis.SurvivalAnalysis_Horse_Core>> sorted =
                survivalAnalysis_horses.stream().collect(Collectors.groupingBy(e->e.toPlaceDistanceCourse(),
                Collectors.mapping(e->e.toHorseIdFinishTime(),Collectors.toList())));

        for(Map.Entry<SurvivalAnalysis.SurvivalAnalysis_Core, List<SurvivalAnalysis.SurvivalAnalysis_Horse_Core>> entry:
                sorted.entrySet() ){
            SurvivalAnalysis.SurvivalAnalysis_Core  racePlaceDistanceCourse = entry.getKey() ;
            List<SurvivalAnalysis.SurvivalAnalysis_Horse_Core> horseIdFinishTime = entry.getValue();
           // System.out.println(racePlaceDistanceCourse.toString());

            if(horseIdFinishTime.size() <=1){
                System.err.println("Only one records. skip...");
            }
            Map<String,List<BigDecimal>> finishTimeByHorseId =horseIdFinishTime.stream().collect(Collectors.groupingBy(e->e.getHorseId(),
                    Collectors.mapping(e->BigDecimal.valueOf(e.getFinishTime()),Collectors.toList())));

            if(finishTimeByHorseId.size() <=1){
                System.err.println("Only one horse. skip...");
            }

            //all horseId
            List<String> allHorseId = new ArrayList<>(finishTimeByHorseId.keySet());
            Collections.sort(allHorseId);
            int i = 0;

            Map<String,Map<String, StatUtils.SurvivalAnalysisResult>> resultOfACourse=
                    new HashMap<String,Map<String, StatUtils.SurvivalAnalysisResult>>();

            for(String horseB: allHorseId){
                List<BigDecimal> horseBFinishTime =finishTimeByHorseId.get(horseB);
                Map<String,StatUtils.SurvivalAnalysisResult> comparedTo= new HashMap<>();
                int j = i+1;
                for(;j<allHorseId.size();j++){

                //for(Map.Entry<String,List<BigDecimal>> oneHorseEntry :  finishTimeByHorseId.entrySet()){

                    String horseA = allHorseId.get(j);
                    List<BigDecimal>  horseAFinishTime = finishTimeByHorseId.get(horseA);

//                    System.out.println(racePlaceDistanceCourse.toString() +","+ horseA +","+HorseAFinishTime.size()+","
//                            + horseB + "," + HorseBFinishTime.size()+","
//                            + StatUtils.logRankTest(HorseAFinishTime,HorseBFinishTime));

                    comparedTo.put(horseA,StatUtils.logRankTest(horseAFinishTime,horseBFinishTime));
                }
                i++;
                resultOfACourse.put(horseB,comparedTo);
            }
            result.put(racePlaceDistanceCourse,resultOfACourse);

        }
        return result;
    }

    public Map<Integer,Map<Integer, StatUtils.SurvivalAnalysisResult>> testLogRankTest_Draw(String sql){
        Map<Integer,Map<Integer, StatUtils.SurvivalAnalysisResult>> result=
                new HashMap<Integer,Map<Integer, StatUtils.SurvivalAnalysisResult>>();
//        String sql =
//                "select racedate, draw,finishTime,distance, racemeeting, upper(course) as course  from racecard "+
//                        " where  distance  in ('1400') "+
//                        " and racemeeting ='ST' and course ='TURF - \"C\" COURSE' "+
//                        " and racedate < 20190530 ";

        List<SurvivalAnalysis.SurvivalAnalysis_Draw>  survivalAnalysis_horses= dao.querySurvivalAnalysis_Draw(sql);

        //Split by racemeeting, distance,course,
        //B010
        Map<SurvivalAnalysis.SurvivalAnalysis_Core, List<SurvivalAnalysis.SurvivalAnalysis_Draw_Core>> sorted =
                survivalAnalysis_horses.stream().collect(Collectors.groupingBy(e->e.toPlaceDistanceCourse(),
                        Collectors.mapping(e->e.toDrawFinishTime(),Collectors.toList())));

        for(Map.Entry<SurvivalAnalysis.SurvivalAnalysis_Core, List<SurvivalAnalysis.SurvivalAnalysis_Draw_Core>> entry:
                sorted.entrySet() ){
            SurvivalAnalysis.SurvivalAnalysis_Core  racePlaceDistanceCourse = entry.getKey() ;
            List<SurvivalAnalysis.SurvivalAnalysis_Draw_Core> drawFinishTime = entry.getValue();
            // System.out.println(racePlaceDistanceCourse.toString());

            if(drawFinishTime.size() <=1){
                System.err.println("Only one records. skip...");
            }
            Map<Integer,List<BigDecimal>> finishTimeByDraw =drawFinishTime.stream().
                    collect(Collectors.groupingBy(e->e.getDraw(),
                    Collectors.mapping(e->BigDecimal.valueOf(e.getFinishTime()),Collectors.toList())));

            if(finishTimeByDraw.size() <=1){
                System.err.println("Only one draw. skip...");
            }

            //all horseId
            List<Integer> allDraw = new ArrayList<>(finishTimeByDraw.keySet());
            Collections.sort(allDraw);
            int i = 0;

            for(Integer drawB: allDraw){
                List<BigDecimal> drawBFinishTime =finishTimeByDraw.get(drawB);
                Map<Integer,StatUtils.SurvivalAnalysisResult> comparedTo= new HashMap<>();
                int j = i+1;
                for(;j<allDraw.size();j++){

                    Integer drawA = allDraw.get(j);
                    List<BigDecimal>  drawAFinishTime = finishTimeByDraw.get(drawA);

//                    System.out.println(racePlaceDistanceCourse.toString() +","+ drawA +","+drawAFinishTime.size()+","
//                            + drawB + "," + drawBFinishTime.size()+","
//                            + StatUtils.logRankTest(drawAFinishTime,drawBFinishTime));

                    comparedTo.put(drawA,StatUtils.logRankTest(drawAFinishTime,drawBFinishTime));
                }
                i++;
                result.put(drawB,comparedTo);
            }
        }
        return result;
    }
    public Map<String,Map<String, StatUtils.SurvivalAnalysisResult>> testLogRankTest_Jockey(String sql){
        Map<String,Map<String, StatUtils.SurvivalAnalysisResult>> result=
                new HashMap<String,Map<String, StatUtils.SurvivalAnalysisResult>>();
//        String sql =
//                "select racedate, jockey,finishTime,distance, racemeeting, upper(course) as course  from racecard "+
//                        " where  distance  in ('1400') "+
//                        " and racemeeting ='ST' and course ='TURF - \"C\" COURSE' "+
//                        " and jockey in ('K Teetan','C Schofield','M L Yeung','A Domeyer'," +
//                        " 'M F Poon','A Sanna','J Moreira','M Chadwick','K H Chan','K C Leung') "+
//                        " and racedate < 20190530 ";

        List<SurvivalAnalysis.SurvivalAnalysis_Jockey>  survivalAnalysis_jockey= dao.querySurvivalAnalysis_Jockey(sql);

        //Split by racemeeting, distance,course,
        //B010
        Map<SurvivalAnalysis.SurvivalAnalysis_Core, List<SurvivalAnalysis.SurvivalAnalysis_Jockey_Core>> sorted =
                survivalAnalysis_jockey.stream().collect(Collectors.groupingBy(e->e.toPlaceDistanceCourse(),
                        Collectors.mapping(e->e.toJockeyFinishTime(),Collectors.toList())));

        for(Map.Entry<SurvivalAnalysis.SurvivalAnalysis_Core, List<SurvivalAnalysis.SurvivalAnalysis_Jockey_Core>> entry:
                sorted.entrySet() ){
            SurvivalAnalysis.SurvivalAnalysis_Core  racePlaceDistanceCourse = entry.getKey() ;
            List<SurvivalAnalysis.SurvivalAnalysis_Jockey_Core> drawFinishTime = entry.getValue();
            // System.out.println(racePlaceDistanceCourse.toString());

            if(drawFinishTime.size() <=1){
                System.err.println("Only one records. skip...");
            }
            Map<String,List<BigDecimal>> finishTimeByJockey =drawFinishTime.stream().
                    collect(Collectors.groupingBy(e->e.getJockey(),
                            Collectors.mapping(e->BigDecimal.valueOf(e.getFinishTime()),Collectors.toList())));

            if(finishTimeByJockey.size() <=1){
                System.err.println("Only one Jockey. skip...");
            }

            //all jockey
            List<String> allJockey = new ArrayList<>(finishTimeByJockey.keySet());
            Collections.sort(allJockey);
            int i = 0;

            for(String jockeyB: allJockey){
                List<BigDecimal> jockeyBFinishTime =finishTimeByJockey.get(jockeyB);
                int j = i+1;

                Map<String,StatUtils.SurvivalAnalysisResult> comparedTo= new HashMap<>();
                for(;j<allJockey.size();j++){

                    String jockeyA = allJockey.get(j);
                    List<BigDecimal>  jockeyAFinishTime = finishTimeByJockey.get(jockeyA);

//                    System.out.println(racePlaceDistanceCourse.toString() +","+ jockeyA +","+jockeyAFinishTime.size()+","
//                            + jockeyB + "," + jockeyBFinishTime.size()+","
//                            + StatUtils.logRankTest(jockeyAFinishTime,jockeyBFinishTime));


                    comparedTo.put(jockeyA,StatUtils.logRankTest(jockeyAFinishTime,jockeyBFinishTime));

                }
                i++;
                result.put(jockeyB,comparedTo);
            }
        }
        return result;
    }

    public void testLogRankTestOfARace(List<RaceCardResult> allRaceCard){
        if(allRaceCard.isEmpty()){
            return;
        }
        //All horse of a race
        List<StatUtils.SurvivalAnalysisResultWrapper> result = new ArrayList<>();
        RaceCardResult raceInfo = allRaceCard.get(0);
        String course = raceInfo.getCourse();
        int distance = raceInfo.getDistance();
        String raceMeeting = raceInfo.getRacePlace();
        String raceDate =  raceInfo.getRaceDate();
        SurvivalAnalysis.SurvivalAnalysis_Core survivalAnalysis_raceInfo = new SurvivalAnalysis.SurvivalAnalysis_Core();
        survivalAnalysis_raceInfo.setCourse(course);
        survivalAnalysis_raceInfo.setDistance(distance);
        survivalAnalysis_raceInfo.setRacePlace(raceMeeting);

        StringBuilder allJockey = new StringBuilder();
        StringBuilder allHorse = new StringBuilder();
        int i=0;
        for(RaceCardItem item: allRaceCard){
            if(i++ >0){
                allJockey.append(",");

                allHorse.append(",");
            }
            allJockey.append("'").append(item.getJockey()).append("'");
            allHorse.append("'").append(item.getHorseId()).append("'");
        }

        String sql_jockey=
                "select racedate, jockey,finishTime,distance, racemeeting, upper(course) as course  from racecard "+
                        " where  distance  in ("+ distance +") "+
                        " and racemeeting ='"+raceMeeting+"' and course ='"+ course+"' "+
                        " and jockey in ("+ allJockey+") "+
                        " and racedate < " + raceDate ;


        String sql_draw =
                "select racedate, draw,finishTime,distance, racemeeting, upper(course) as course  from racecard "+
                        " where  distance  in ("+ distance +") "+
                        " and racemeeting ='"+raceMeeting+"' and course ='"+ course+"' "+
                        " and racedate < " + raceDate ;

        String sql_horse =
                "select racedate, horseId,finishTime,distance, racemeeting, upper(course) as course  from racecard "+
                        " where  distance  in ("+ distance +") "+
                        " and horseId in ("+ allHorse +") "+
                        " and racedate < " + raceDate ;


        Map<String,Map<String, StatUtils.SurvivalAnalysisResult>> jockeyResultAll = this.testLogRankTest_Jockey(sql_jockey);
        Map<Integer,Map<Integer, StatUtils.SurvivalAnalysisResult>> drawResultAll = this.testLogRankTest_Draw(sql_draw);

        Map<SurvivalAnalysis.SurvivalAnalysis_Core,Map<String,Map<String, StatUtils.SurvivalAnalysisResult>>>
                horseResultAll = this.testLogRankTest_horse(sql_horse);

        Collections.sort(allRaceCard, new Comparator<RaceCardItem>() {
            @Override
            public int compare(RaceCardItem o1, RaceCardItem o2) {
               return o2.getHorseNo().compareTo(o1.getHorseNo());
            }
        });

        int j,k=0;
        for(j=0;j<allRaceCard.size() -1; j++){

            RaceCardResult itemA = allRaceCard.get(j);
            String horseIdA = itemA.getHorseId();
            int drawA = itemA.getDraw();
            String jockeyA = itemA.getJockey();
            String horseNoA = itemA.getHorseNo();

            for(k=j+1; k<allRaceCard.size();k ++){
                RaceCardResult itemB = allRaceCard.get(k);
                String horseIdB = itemB.getHorseId();
                int drawB = itemB.getDraw();
                String jockeyB = itemB.getJockey();
                String horseNoB = itemB.getHorseNo();
                System.out.print(horseNoA + ","+horseNoB);


                StatUtils.SurvivalAnalysisResultWrapper aResult = new StatUtils.SurvivalAnalysisResultWrapper();
                aResult.setRaceInfo(raceInfo);
                aResult.setHorseIdA(horseIdA);
                aResult.setHorseIdB(horseIdB);
                aResult.setHorseNoA(itemA.getHorseNo());
                aResult.setHorseNoB(itemB.getHorseNo());

                aResult.setDrawA(drawA);
                aResult.setDrawB(drawB);
                aResult.setJockeyA(jockeyA);
                aResult.setJockeyB(jockeyB);

                if(itemA.getPlace() == 0 || itemB.getPlace()==0){
                    aResult.setResult(0);
                } else {
                    if (itemA.getPlace() < itemB.getPlace()) {
                        aResult.setResult(1); //A is better than B
                    } else {
                        aResult.setResult(2); //B is better than A
                    }
                }

                aResult.setFinishTimeA(itemA.getFinishTime());
                aResult.setFinishTimeB(itemB.getFinishTime());


                ///////////////////////////////Jockey Result //////////////////////////////
                StatUtils.SurvivalAnalysisResult jockeyResult= jockeyResultAll.get(jockeyB) ==null ? null:
                        jockeyResultAll.get(jockeyB).get(jockeyA);
                if(jockeyResult==null){
                    jockeyResult= jockeyResultAll.get(jockeyA) ==null ? null:
                            jockeyResultAll.get(jockeyA).get(jockeyB);
                    if(jockeyResult!=null){
                        jockeyResult.setRelativeRisk_A2B(BigDecimal.ONE.divide(jockeyResult.getRelativeRisk_A2B(),SCALE, RoundingMode.HALF_UP));
                        int countA= jockeyResult.getCountA();
                        int countB = jockeyResult.getCountB();
                        jockeyResult.setCountA(countB);
                        jockeyResult.setCountB(countA);
                    }
                }

                aResult.setJockeyResult(jockeyResult);
                ///////////////////////////////Draw Result //////////////////////////////
                StatUtils.SurvivalAnalysisResult drawResult= drawResultAll.get(drawB) ==null ? null:
                        drawResultAll.get(drawB).get(drawA);

                if(drawResult==null){
                    drawResult= drawResultAll.get(drawA) ==null ? null:
                            drawResultAll.get(drawA).get(drawB);
                    if(drawResult!=null){
                        drawResult.setRelativeRisk_A2B(BigDecimal.ONE.divide(drawResult.getRelativeRisk_A2B(),SCALE, RoundingMode.HALF_UP));
                        int countA= drawResult.getCountA();
                        int countB = drawResult.getCountB();
                        drawResult.setCountA(countB);
                        drawResult.setCountB(countA);
                    }
                }
                aResult.setDrawResult(drawResult);

                ///////////////////////////////Horse Result _ same distance //////////////////////////////
                StatUtils.SurvivalAnalysisResult horseResult_Same = horseResultAll.get(survivalAnalysis_raceInfo) ==null? null:
                        (horseResultAll.get(survivalAnalysis_raceInfo).get(horseIdB) ==null ? null:
                                horseResultAll.get(survivalAnalysis_raceInfo).get(horseIdB).get(horseIdA));

                if(horseResult_Same==null){
                    horseResult_Same=
                            horseResultAll.get(survivalAnalysis_raceInfo) ==null? null:
                                    (horseResultAll.get(survivalAnalysis_raceInfo).get(horseIdA) ==null ? null:
                                            horseResultAll.get(survivalAnalysis_raceInfo).get(horseIdA).get(horseIdB));
                    if(horseResult_Same!=null){
                        horseResult_Same.setRelativeRisk_A2B(BigDecimal.ONE.divide(horseResult_Same.getRelativeRisk_A2B(),
                                SCALE, RoundingMode.HALF_UP));
                        int countA= horseResult_Same.getCountA();
                        int countB = horseResult_Same.getCountB();
                        horseResult_Same.setCountA(countB);
                        horseResult_Same.setCountB(countA);
                    }
                }
                aResult.setSameHorseResult(horseResult_Same);
                ///////////////////////////////Horse Result _ diff distance //////////////////////////////
                List<StatUtils.SurvivalAnalysisResult> horseResult_difference_list = new ArrayList<>();
                for(Map.Entry<SurvivalAnalysis.SurvivalAnalysis_Core,Map<String,Map<String, StatUtils.SurvivalAnalysisResult>>>
                        entry : horseResultAll.entrySet()){
                    SurvivalAnalysis.SurvivalAnalysis_Core survivalAnalysis_raceInfo_other = entry.getKey();
                    if(survivalAnalysis_raceInfo_other.equals(survivalAnalysis_raceInfo)){
                        continue;
                    }
                    Map<String,Map<String, StatUtils.SurvivalAnalysisResult>> otherResult = entry.getValue();
                    StatUtils.SurvivalAnalysisResult horseResult_Other = otherResult.get(horseIdB)==null? null: otherResult.get(horseIdB).get(horseIdA);

                    if(horseResult_Other ==null){
                        horseResult_Other=
                                otherResult.get(horseIdA) ==null ? null:
                                        otherResult.get(horseIdA).get(horseIdB);
                        if(horseResult_Other!=null) {
                            horseResult_Other.setRelativeRisk_A2B(BigDecimal.ONE.divide(horseResult_Other.getRelativeRisk_A2B(),
                                    SCALE, RoundingMode.HALF_UP));
                            int countA = horseResult_Other.getCountA();
                            int countB = horseResult_Other.getCountB();
                            horseResult_Other.setCountA(countB);
                            horseResult_Other.setCountB(countA);
                        }
                    }

                    if(horseResult_Other !=null){
                        horseResult_difference_list.add(horseResult_Other);
                    }

                }

                if(horseResult_difference_list!=null && !horseResult_difference_list.isEmpty()){
                    if(horseResult_difference_list.size() ==1){
                        aResult.setDiffHorseResult(horseResult_difference_list.get(0));
                    } else {
                        Collections.sort(horseResult_difference_list, new
                                Comparator<StatUtils.SurvivalAnalysisResult>() {
                                    @Override
                                    public int compare(StatUtils.SurvivalAnalysisResult o1, StatUtils.SurvivalAnalysisResult o2) {
                                      return  (o2.getCountA() + o2.getCountB()) -  (o1.getCountA() + o1.getCountB());
                                    }
                                });
                        aResult.setDiffHorseResult(horseResult_difference_list.get(0));
                    }
                }
                result.add(aResult);
                if(jockeyResult!=null){
                    System.out.print("," + jockeyResult.getCountA() +":"+jockeyResult.getCountB() +":"+jockeyResult.getRelativeRisk_A2B() );
                } else {
                    System.out.print(",");
                }
                if(drawResult!=null){
                    System.out.print("," + drawResult.getCountA() +":"+drawResult.getCountB() +":"+drawResult.getRelativeRisk_A2B() );
                } else {
                    System.out.print(",");
                }

                if(horseResult_Same!=null){
                    System.out.print("," + horseResult_Same.getCountA() +":"+horseResult_Same.getCountB() +":"+horseResult_Same.getRelativeRisk_A2B() );
                } else {
                    System.out.print(",");
                }

                for( StatUtils.SurvivalAnalysisResult horseResult_Other : horseResult_difference_list){
                    System.out.print("," + horseResult_Other.getCountA() +":"+horseResult_Other.getCountB() +":"+horseResult_Other.getRelativeRisk_A2B() );

                }
                System.out.println();

            }

        }

        dao.batchInsertSurvialAnalysis(result);

    }

    public void buildAnalysis_HistoryRecord(String daysFrom, String daysOnAfter){
        List<String> allDates = dao.getRaceDates(daysFrom, daysOnAfter);
        for(String aDate: allDates){
            System.out.println("build Statics for race day:"+aDate);
            int raceNumber = dao.getRaceSeqOfDay(aDate);
            for(int i=1; i<=raceNumber;i++){
                this.buildAnalysis_HistoryRecordOfARace(aDate,i);
            }

        }


    }

    public void buildAnalysis_HistoryRecordOfARace(String raceDate, int raceSeqOfADay){

       dao.runSQL("delete from survival_analysis where raceDate='"+ raceDate+ "' and seq="+raceSeqOfADay);
       List<RaceCardResult> allRaceCardResult =dao.queryForSurvivalAnalysis(
               "select * from racecard where raceDate='"+raceDate+"'" +
               "and raceSeqOfDay="+raceSeqOfADay);


        this.testLogRankTestOfARace(allRaceCardResult);

    }
}
