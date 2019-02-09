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

/*Step 1: get History record and new Race*/
//       DataCrawler crawler = context.getBean(DataCrawler.class);
//        crawler.getRecord();
//        crawler.getNewRaceCard();
        /*Step 1.1. get dividend for regression */
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

/* Step2 : Build statistic for history and new race*/
//       Statistics stat = context.getBean(Statistics.class);
//       stat.buildStatistics_allRaceCards("20190207","20190207");
//       stat.statisticAllNewRace();


/* Step3 : generate csv file for history and new race*/
//        Predictor predictor = context.getBean(Predictor.class);
//        predictor.predictNewRace();
      //  predictor.regressionOfRaceCard("select * from racecard", "history");
//        predictor.regressionOfRaceCard("select * from racecard where racedate=20190202 or racedate=20190130", "regress");



        Metric metric = context.getBean(Metric.class);
        metric.checkForcastMetric("20170101","20200101" ,"Metric-2017-2019_20190207");
//       metric.checkForcastMetric("20190109","20190109" ,"test");


    }

}
