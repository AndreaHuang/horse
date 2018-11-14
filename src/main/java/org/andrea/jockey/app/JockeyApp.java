package org.andrea.jockey.app;



import org.andrea.jockey.crawler.DataCrawler;
import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@ComponentScan
@EnableAutoConfiguration
@Configuration
@Component
public class JockeyApp {

    public static void main(String[] args) {


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        RecordCardDAO dao = context.getBean(RecordCardDAO.class);

        DataCrawler crawler = context.getBean(DataCrawler.class);
        //crawler.getRecord();

        crawler.getNewRaceCard();
        String url =null;

        //20170913


     // url= "https://racing.hkjc.com/racing/info/meeting/Results/English/Local/20180905/HV";
      //  crawler.getRecordsByUrlOfDay(url);
      // crawler.getRecordOfARace(url);
      //  crawler.getRecordOfARace("http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171111/ST/9");
      //  crawler.getRecordOfARace("http://racing.hkjc.com/racing/Info/meeting/Results/English/Local/20171111/ST/10");


    }

}
