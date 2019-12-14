package org.andrea.jockey.app;



import org.andrea.jockey.predict.FirstFour;
import org.andrea.jockey.predict.Metric;
import org.andrea.jockey.predict.Predictor_SurvivalAnalysis;
import org.andrea.jockey.statistics.Statistic_SurvivalAnalysis;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


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
        int distance =1800;
        int raceClass=4;
        String course= null; //"TURF - \"C\" COURSE";
        String fileName = String.join("-","FirstFour",String.valueOf(distance),
        String.valueOf(raceClass),course,fromDate,toDate, SDF.format(new Date()));
        first4.calGains(fromDate,toDate,fileName,distance,raceClass,course);
    }

}
