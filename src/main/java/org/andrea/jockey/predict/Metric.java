package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Metric {

    @Autowired
    RecordCardDAO dao;


    public void checkForcastMetric(String fromDate, String toDate,String fileName){

        FileWriter writer = null;
        File f =null;

        try {

           List<String> racesDates =  dao.getRaceDates(fromDate,toDate);

            f = new File(fileName+".csv");
            writer = new FileWriter(f);
            writer.write("RaceDate,SeqOfDay,Distance,Class,Going,ActualPlace,Bingo");
            writer.write("\r\n");


            for(String raceDate: racesDates){

                this.checkForcastMetric(writer, raceDate);

            }
            writer.close();
            writer= null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = null;
            }
        }
    }
    private void checkForcastMetric(FileWriter writer, String raceDate) throws IOException {
        System.out.println("checking "+raceDate);
        int raceNumber = dao.getRaceSeqOfDay(raceDate);
        for (int i = 1; i <= raceNumber; i++) {
            this.statisticRaceCard(writer,raceDate, i);
        }
    }

    private void statisticRaceCard(FileWriter writer,String raceDate, int seqOfDay) throws IOException {

        List<RaceCardResult> resultList = dao.queryRaceResult("select * from racecard " +
                "where racedate = "+ raceDate +" and raceSeqOfDay =" +seqOfDay +
                " and predicted_place is not null "+
                " order by predicted_place asc");

        if(resultList.isEmpty()){
            System.err.println("Empty Result:"+ raceDate +":"+seqOfDay);
            return;
        }
        StringBuilder sb = new StringBuilder(8);

        int i=0;
        int bingo=0;
        for(RaceCardResult aItem : resultList){
            sb.append(aItem.getPlace()).append("-");

            if(i++<3){
                if(aItem.getPredicted_place()>0 && aItem.getPlace()<=3){
                    bingo++;
                }
            }

        }

        writer.write(raceDate +","+seqOfDay+","+resultList.get(0).getDistance()+","+
                resultList.get(0).getRaceClass()+","+resultList.get(0).getGoing()+","
                +sb.toString()+","+bingo);
        writer.write("\r\n");


    }
}
