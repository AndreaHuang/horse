package org.andrea.jockey.app;



import org.andrea.jockey.predict.Metric;
import org.andrea.jockey.predict.Predictor;
import org.andrea.jockey.crawler.DataCrawler;
import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.statistics.Statistics;
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

    public static void main(String[] args) throws Exception{


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        /*Step 1: get History record and new Race*/
//       DataCrawler crawler = context.getBean(DataCrawler.class);
//       String []raceDate = crawler.getRecord();
//       crawler.getNewRaceCard();
        /* Step2 : Build statistic for history and new race*/
    //  Statistics stat = context.getBean(Statistics.class);
//       if(raceDate.length >0) {
//           stat.buildStatistics_allRaceCards(raceDate[0], raceDate[1]);
//       }
//       stat.statisticAllNewRace();

     //   stat.buildStatistics_draw();



//        stat.buildStatistics_allRaceCards("20000217", "20200217");

//        crawler.getDividendsByUrlOfRace("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181118/ST/3");
 //      crawler.getDividendsByUrlOfRace("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181216/ST/8");


        Metric metric = context.getBean(Metric.class);
        metric.checkForcastMetric("20170101","20200101" ,"Metric-2017-2019_"+SDF.format(new Date()));
//        metric.checkForcastMetric("20181114","20181114" ,"test");




/* Step3 : generate csv file for history and new race*/
//        Predictor predictor = context.getBean(Predictor.class);
//        predictor.predictNewRace();
      //  predictor.regressionOfRaceCard("select * from racecard", "history");
//        predictor.regressionOfRaceCard("select * from racecard where racedate=20190202 or racedate=20190130", "regress");






    }

}
