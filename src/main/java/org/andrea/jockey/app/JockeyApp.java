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



@ComponentScan
@EnableAutoConfiguration
@Configuration
@Component
public class JockeyApp {

    public static void main(String[] args) throws Exception{


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


       DataCrawler crawler = context.getBean(DataCrawler.class);
//        crawler.getRecord();
//        crawler.getNewRaceCard();
//       crawler.getDividends(20171011);
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171126/ST");
//
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171223/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180128/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180221/HV");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180318/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180328/HV");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180429/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180506/ST");

//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180527/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180603/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180613/HV");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180616/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180624/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20180909/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181028/HV");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181114/HV");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181118/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181125/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20181216/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20190106/ST");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20190116/HV");
//        crawler.getDividendsByUrlOfDay("https://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20190120/ST");


//      String url= "https://racing.hkjc.com/racing/info/meeting/Results/English/Local/20181205/HV";

      //crawler.getRecordOfARace(url);
   //     crawler.getRecordOfARace("http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171111/S");
   //     crawler.getRecordOfARace("http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171111/ST/10");


//        Statistics stat = context.getBean(Statistics.class);
  //    stat.buildStatistics_allRaceCards("20181205");
  //      stat.buildStatistics_allRaceCards("20181206","20191212");
        // stat.buildStatistics_allRaceCards("2017001","20170831");
//       stat.statisticAllNewRace();
//      stat. buildStatistics_Race();
//        stat.statisticForPastRecords(20190112);
//       stat.statisticRaceCard(20181229,7);
//        stat.statisticForPastRecords(20190109);
//        stat.statisticForPastRecords(20190106);
//        stat.statisticForPastRecords(20190101);
//        stat.statisticForPastRecords(20181229);
//        stat.statisticForPastRecords(20181226);
//        stat.statisticForPastRecords(20181223);

//        stat.statisticForPastRecords(20181219);
//        stat.statisticForPastRecords(20181216);
//        stat.statisticForPastRecords(20181212);

//        Predictor predictor = context.getBean(Predictor.class);
//        predictor.regressionOfRaceCard("Select * from racecard ","regression_20190127");



        Metric metric = context.getBean(Metric.class);
        metric.checkForcastMetric("20170101","20200101" ,"Metric-2017-2019_20190207");
//       metric.checkForcastMetric("20190109","20190109" ,"test");


    }

}
