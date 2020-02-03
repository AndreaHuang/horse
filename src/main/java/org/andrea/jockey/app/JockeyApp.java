package org.andrea.jockey.app;



import org.andrea.jockey.predict.FirstFour;
import org.andrea.jockey.predict.FirstFourStrategy;
import org.andrea.jockey.predict.Metric;
import org.andrea.jockey.predict.Predictor_SurvivalAnalysis;
import org.andrea.jockey.statistics.Statistic_SurvivalAnalysis;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ComponentScan
@EnableAutoConfiguration
@Configuration
@Component
public class JockeyApp {
    private static final SimpleDateFormat SDF=new SimpleDateFormat("yyyyMMddhhmmss");
    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    public static void main(String[] args) {

        try {


//           statistic_SurvivalAnalysis(context);

            /*Step 1: get History record and new Race*/
//       DataCrawler crawler = context.getBean(DataCrawler.class);
//      crawler.getDividends(20190213,20190411);
//             String [] raceDate = crawler.getRecord();
//              crawler.getNewRaceCard();




//            /* Step2 : Build statistic for history and new race*/
//            Statistics stat = context.getBean(Statistics.class);
//       if(raceDate.length >0) {
//           stat.buildStatistics_allRaceCards(raceDate[0], raceDate[1]);
//            stat.buildStatistics_draw(raceDate[0], raceDate[1]);
//
//       }
//
//             stat.buildStatistics_allRaceCards("20190414","20190501");
            //stat.statisticAllNewRace();
//            stat.buildStatistics_draw("20190414","20190501");
//            stat.buildStatistics_weight();


//        stat.buildStatistics_allRaceCards("20000217", "20200217");

//        crawler.getDividendsByUrlOfRace("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181118/ST/3");
            //      crawler.getDividendsByUrlOfRace("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181216/ST/8");


            String fromDate = "20170901";
            String toDate = "20191230";
           // statistic_SurvivalAnalysis(fromDate,toDate);
//            calculatePredictedPlace(fromDate,toDate);
//            calculateGains(fromDate,toDate);

            calculateFirs4(fromDate,toDate);





            /* Step3 : generate csv file for history and new race*/
//        Predictor predictor = context.getBean(Predictor.class);
//        predictor.predictNewRace();
            //  predictor.regressionOfRaceCard("select * from racecard", "history");
//        predictor.regressionOfRaceCard("select * from racecard where racedate=20190202 or racedate=20190130", "regress");


        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private static void statistic_SurvivalAnalysis(String fromDate, String toDate ){
        Statistic_SurvivalAnalysis stat = context.getBean(Statistic_SurvivalAnalysis.class);

        stat.buildAnalysis_HistoryRecord(fromDate,toDate);
    }
    private static void calculatePredictedPlace(String fromDate, String toDate ){
        Predictor_SurvivalAnalysis predictor = context.getBean(Predictor_SurvivalAnalysis.class);
        predictor.calPredicatedPlace(fromDate,toDate);
    }

    private static void calculateGains(String fromDate,String toDate){
            Metric metric = context.getBean(Metric.class);
            metric.checkForcastMetric(fromDate,toDate ,"Metric-"+fromDate+"_"+toDate+"_"+SDF.format(new Date()));
    }
    private static void calculateFirs4(String fromDate,String toDate){
        FirstFour first4 = context.getBean(FirstFour.class);
//        int distance =1650;
//        int raceClass=5;
//        String place="HV";
//        String course= "TURF - \"B\" COURSE";
        int distance =0;
        int raceClass=0;
        String place=null;
        String course= null;
//        String course=null;
        //TURF - "B+2" COURSE
        String fileName = String.join("-","FirstFour",String.valueOf(distance),
        String.valueOf(raceClass),course,fromDate,toDate, SDF.format(new Date()));
        first4.calGains(fromDate,toDate,fileName,place, distance,raceClass,course);
    }

    public static class Strategy{

    }

    private static FirstFourStrategy odds_123459A = new FirstFourStrategy(new int[]{0,1,2,3,4,8,9});
    private static FirstFourStrategy odds_1234589 = new FirstFourStrategy(new int[]{0,1,2,3,4,7,8});
    private static FirstFourStrategy odds_123489A = new FirstFourStrategy(new int[]{0,1,2,3,7,8,9});
    private static FirstFourStrategy odds_onepart_4 = new FirstFourStrategy(new int[]{0,1,2,3,4,5});
    private static FirstFourStrategy odds_onepart_5 = new FirstFourStrategy(new int[]{0,1,2,3,4});


    private static FirstFourStrategy odds_1234589_twoPart = new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{7,8},3);

    private static FirstFourStrategy odds_123489A_twoPart = new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{6,7,8},3);

    private static FirstFourStrategy odds_234589A_twoPart = new FirstFourStrategy(
            new int[]{1,2,3,4},new int[]{7,8,9},3);

    private static FirstFourStrategy odds_123456789A_twoPart = new FirstFourStrategy(
            new int[]{1,2,3,4},new int[]{5,6,7,8,9},1);
    private static FirstFourStrategy odds_123456789A_twoPart_Fixed = new FirstFourStrategy(
            new int[]{2,3,4},new int[]{5,6,7,8,9}, new int[]{0,1},1);
    private static FirstFourStrategy odds_123456789A_twoPart_NoFixed= new FirstFourStrategy(
            new int[]{2,3,4},new int[]{5,6,7,8,9},1);

    private static FirstFourStrategy odds_123456789A_twoPart_NoFixed_1600_3= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9},3);

    private static FirstFourStrategy odds_123456789A_twoPart_NoFixed_1000_4_C3= new FirstFourStrategy(
            new int[]{0,1,2,3},new int[]{4,5,6,7},3);

    private static FirstFourStrategy odds_twoPart_2_A= new FirstFourStrategy(
            new int[]{0,1,2,3},new int[]{4,5,6,7,8},2);

    private static FirstFourStrategy odds_twoPart_1= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9},3);
    private static FirstFourStrategy odds_twoPart_2= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9},2);
    private static FirstFourStrategy odds_twoPart_3= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9,10},2);
    private static FirstFourStrategy odds_twoPart_4= new FirstFourStrategy(
            new int[]{0,1,2,3},new int[]{4,5,6,7,8,9,10,11},2);

    private static FirstFourStrategy odds_twoPart_5= new FirstFourStrategy(
            new int[]{0,1,2},new int[]{3,4,5,6,7},2);
    private static FirstFourStrategy odds_twoPart_6= new FirstFourStrategy(
            new int[]{0,1,2,3,4,5},new int[]{6,7,8,9,10,11},2);

    private static FirstFourStrategy odds_twoPart_7= new FirstFourStrategy(
            new int[]{0,1,2,4,5},new int[]{6,7,8,9,10,11,12},2);

    private static FirstFourStrategy odds_twoPart_9= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9},3);



    private static FirstFourStrategy odds_twoPart_2_1 = new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9},2);

    private static FirstFourStrategy odds_twoPart_3_1= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8},3);

    private static FirstFourStrategy odds_twoPart_11= new FirstFourStrategy(
            new int[]{0,1,2,3,4,5},new int[]{6,7,8,9,10},3);
    private static FirstFourStrategy odds_twoPart_3_2= new FirstFourStrategy(
            new int[]{0,1,2,3,4},new int[]{5,6,7,8,9},3);

    private static FirstFourStrategy odds_twoPart_3_3= new FirstFourStrategy(
            new int[]{0,1},new int[]{2,3,4,5,6,7,8},1);
    private static FirstFourStrategy odds_twoPart_2_2= new FirstFourStrategy(
            new int[]{0,1,2,3},new int[]{4,5,6,7,8,9},2);


    //>=12
    public static FirstFourStrategy odds_threePart_211_1= new FirstFourStrategy(
            new int[]{0,1,2,3},new int[]{4,5,6,7,8},new int[]{9,10,11,12},2,1);
    //10,11
    public static FirstFourStrategy odds_threePart_ABBC= new FirstFourStrategy(
            new int[]{0},new int[]{1,2,3,4,5,6,7},new int[]{8,9},1,2);

    public static List<FirstFourStrategy> allStrategy = new ArrayList<>();
    static {
//        allStrategy.add(odds_123459A);
//        allStrategy.add(odds_1234589);
//        allStrategy.add(odds_123489A);
//        allStrategy.add(odds_twoPart_2);
//        allStrategy.add(odds_twoPart_9);
//        allStrategy.add(odds_twoPart_10);

//        allStrategy.add(odds_twoPart_6);

//        allStrategy.add(odds_twoPart_2_A);
        allStrategy.add(odds_threePart_ABBC);

//        allStrategy.add(odds_onepart_4);
//        allStrategy.add(odds_123456789A_twoPart_NoFixed);


    }

}
