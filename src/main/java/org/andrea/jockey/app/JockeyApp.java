package org.andrea.jockey.app;



import org.andrea.jockey.predict.Predictor;
import org.andrea.jockey.crawler.DataCrawler;
import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.statistics.Statistics;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@ComponentScan
@EnableAutoConfiguration
@Configuration
@Component
public class JockeyApp {

    public static void main(String[] args) {


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


//        DataCrawler crawler = context.getBean(DataCrawler.class);
//        crawler.getRecord();
//
//        crawler.getNewRaceCard();


      //String url= "https://racing.hkjc.com/racing/info/meeting/Results/English/Local/20170627/HV";
     // crawler.getRecordsByUrlOfDay(url);
      //crawler.getRecordOfARace(url);
      //  crawler.getRecordOfARace("http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171111/ST/9");
      //  crawler.getRecordOfARace("http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171111/ST/10");




        Statistics stat = context.getBean(Statistics.class);
        //stat.statisticAllNewRace();
//        stat.statisticForPastRecords(20190112);
//        stat.statisticForPastRecords(20190109);
//        stat.statisticForPastRecords(20190106);
//        stat.statisticForPastRecords(20190101);
        stat.statisticForPastRecords(20181229);
        stat.statisticForPastRecords(20181226);
        stat.statisticForPastRecords(20181223);

//        stat.statisticForPastRecords(20181219);
//        stat.statisticForPastRecords(20181216);
//        stat.statisticForPastRecords(20181212);

        Predictor predictor = context.getBean(Predictor.class);
        predictor.regressionOfRaceCard("Select * from racecard where racedate >='20181212'",
                "regression");

    }

}
