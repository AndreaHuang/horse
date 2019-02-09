package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.NewRaceRowMapper;
import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.RaceCardAnalysis;

import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Predictor {
    private static final String SQL_SAME_COURSE_SAME_DISTANCT="select racecard.*,newRace.hourseNo as newHorseNo, newRace.rating as newRating,newRace.ratingDelta from newrace inner join racecard " +
            " on newrace.horseid = racecard.horseid and newrace.course=racecard.course "+
            " and newrace.distance = racecard.distance " +
            " and newrace.raceClass = racecard.raceClass " +
            " and newrace.raceSeqOfDay= {0}"+
            " order by newRace.hourseNo asc,racecard.ratedate desc";
    private static final String SQL_DIFFERENT_COURSE_DIFFERENT_DISTANCT="select " +
            " racecard.rateDate,racecard.raceMeeting ,racecard.raceId,racecard.raceSeqOfDay," +
            " racecard.raceClass,racecard.distance,racecard.going,racecard.course, " +
            " racecard.draw,racecard.horseId,racecard.horseName,racecard.jockey,"+
            " racecard.trainer,racecard.addedWeight,racecard.declaredHorseWeight,"+
            " racecard.lbs,racecard.winOdds," +
            " racecard.place,racecard.runningPosition, "+
            " (racecard.finishTime * newRace.distance / racecard.distance) as finishTime,  " +
            " newRace.hourseNo as newHorseNo, newRace.rating as newRating,newRace.ratingDelta from newrace inner join racecard " +
            " on newrace.horseid = racecard.horseid and (newrace.course!=racecard.course "+
            " or newrace.distance != racecard.distance " +
            " or newrace.raceClass != racecard.raceClass) " +
            " and newrace.raceSeqOfDay= {0} " +
            " order by newRace.hourseNo asc,racecard.ratedate desc";
    private static final String SQL_FINISH_TIME="select min(racecard.finishTime) " +
            " from newrace inner join racecard " +
            " on newrace.course=racecard.course and newrace.raceClass=racecard.raceClass "+
            " and newrace.distance = racecard.distance " +
            " where raceCard.place=1 and newrace.raceSeqOfDay={0}" +
            " group by racecard.raceClass,racecard.course,racecard.distance  ";

    private static final String SQL_HISTORICAL_OF_NEWRACE="select racecard.* " +
            " from newrace inner join racecard " +
            " on newrace.horseid = racecard.horseid "+
            " and newrace.raceSeqOfDay= {0}";

    private static final String SQL_NEWRACE="select * " +
            " from newrace  " +
            " where newrace.raceSeqOfDay= {0}";

    @Autowired
    RecordCardDAO dao;

    @Deprecated
    public void predictAll(){
        int raceDate = dao.getNewRaceDate();
        int raceNumber = dao.getNewRaceNumber();
        for(int i=1; i<=raceNumber;i++){
            this.predictARace2(raceDate,i);
        }
    }
    @Deprecated
    private void predictARace(int date, int seqOfDay) {
        System.out.println("Race :" + seqOfDay);
        /*Finish Time statistics*/
        Double standardFinishTime = dao.queryForDouble(getSQL_StandardFinishTime(seqOfDay));
        /* same course same distance*/
        List<RaceCardAnalysis> result = dao.query(getSQL_SameCourseSameDistance(seqOfDay));
        FileWriter writer = null;
        try {
            File f = new File(date+"_"+seqOfDay + "_SameDistance_SameCourse.csv");
            writer = new FileWriter(f);


            this.printHeader(writer);
            for (RaceCardAnalysis a : result) {
                a.setStandardFinishTime(standardFinishTime);
                this.printAnalysis(writer, a);
            }
            result = dao.query(getSQL_DifferentCourseDifferentDistance(seqOfDay));
            writer.close();

            f = new File(date+"_"+seqOfDay + "_DifferentCourse_DifferentDistance.csv");
            writer = new FileWriter(f);
            this.printHeader(writer);
            for (RaceCardAnalysis a : result) {
                a.setStandardFinishTime(standardFinishTime);
                this.printAnalysis_DifferentCourse_DifferentDistance(writer, a);
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
    @Deprecated
    private void predictARace2(int date, int seqOfDay) {
        System.out.println("Race :" + seqOfDay);
         /* same course same distance*/
        List<RaceCardResult> raceCard = null;
        List<RaceCardItem> newRace = null;
        FileWriter writer = null;
        try {
            newRace = dao.queryNewRace(getSQL_NewRace(seqOfDay));
            File f = new File(date+"_"+seqOfDay + "_predict.csv");
            writer = new FileWriter(f);


            this.printNewRaceHeader(writer);
            for (RaceCardItem a : newRace) {
                this.printNewRace(writer, a);
            }
            raceCard = dao.queryRaceResult(getSQL_History_of_NewRace(seqOfDay));
            writer.close();

            f = new File(date+"_"+seqOfDay + "_history.csv");
            writer = new FileWriter(f);
            this.printRaceCardHeader(writer);
            for (RaceCardResult a : raceCard) {
                this.printRaceCard(writer, a);
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

    public void predictNewRace() {

        int raceDate = dao.getNewRaceDate();

        List<RaceCardItem> newRaces = null;

        FileWriter writer = null;
        File f =null;
        try {
            newRaces = dao.queryNewRace("select * from newRace");

            f = new File("predict.csv");
            writer = new FileWriter(f);
            this.printNewRaceHeader(writer);
            for (RaceCardItem newRace : newRaces) {
                this.printNewRace(writer, newRace);
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

    public void regressionOfRaceCard(String sql,String fileName) {

        List<RaceCardResult> raceCard = null;

        FileWriter writer = null;
        File f =null;
        try {
            raceCard = dao.queryRaceResult(sql);

            f = new File(fileName+".csv");
            writer = new FileWriter(f);
            this.printRaceCardHeader(writer);
            for (RaceCardResult a : raceCard) {
                this.printRaceCard(writer, a);
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
    private String getSQL_NewRace(int seqOfDay){
        String s = SQL_NEWRACE.replace("{0}",Integer.toString(seqOfDay));
        return s;
    }
    private String getSQL_History_of_NewRace(int seqOfDay){
        String s = SQL_HISTORICAL_OF_NEWRACE.replace("{0}",Integer.toString(seqOfDay));
        return s;
    }
    private String getSQL_DifferentCourseDifferentDistance(int seqOfDay){
        String s = SQL_DIFFERENT_COURSE_DIFFERENT_DISTANCT.replace("{0}",Integer.toString(seqOfDay));
       // System.out.println(s);
        return s;
    }
    private String getSQL_SameCourseSameDistance(int seqOfDay){
        String s = SQL_SAME_COURSE_SAME_DISTANCT.replace("{0}",Integer.toString(seqOfDay));
      //  System.out.println(s);
        return s;
    }

    private String getSQL_StandardFinishTime(int seqOfDay){
        return SQL_FINISH_TIME.replace("{0}",Integer.toString(seqOfDay));
    }

    private void printRaceCardHeader(FileWriter writer) throws IOException {
        writer.write("raceSeqOfDate,raceMeeting,horseId,horseName,rateDate,draw," +
                "distance,course,raceClass,rating,jockey,going,addedWeight,declaredHorseWeight," +
                "finishTime,place,lbw,winOdds,horse_winPer,horse_winCount,horse_newHorse,horse_newDistance," +
                "jockey_winPer,jockey_winCount,horse_last4SpeedRate,horse_latestSpeedRate,Days_from_lastRace");
        writer.write("\r\n");
    }
    private void printNewRaceHeader(FileWriter writer) throws IOException {
        writer.write("raceSeqOfDate,raceMeeting,horseNo,horseId,horseName,rateDate,draw," +
                "distance,course,raceClass,rating,jockey,going,addedWeight,declaredHorseWeight," +
                "horse_winPer,horse_winCount,horse_newHorse,horse_newDistance," +
                "jockey_winPer,jockey_winCount,horse_last4SpeedRate,horse_latestSpeedRate,Days_from_lastRace");
        writer.write("\r\n");
    }
    private void printRaceCard(FileWriter writer, RaceCardResult r) throws IOException {
        writer.write(String.join(",",Integer.toString(r.getRaceSeqOfDay()),r.getRacePlace(),r.getHorseId(),r.getHorseName(),r.getRaceDate(),
                Integer.toString(r.getDraw()), Integer.toString(r.getDistance()),r.getCourse(),
                Integer.toString(r.getRaceClass()),
                Integer.toString(r.getRating()),r.getJockey(),r.getGoing(),
                Integer.toString(r.getAddedWeight()),Integer.toString(r.getDeclaredHorseWeight()),
                Double.toString(r.getFinishTime()),
                Integer.toString(r.getPlace()),Double.toString(r.getLbw()),
                Double.toString(r.getWinOdds()),
                Double.toString(r.getHorse_winPer()),
                Integer.toString(r.getHorse_winCount()),
                Integer.toString(r.getHorse_newHorse()),
                Integer.toString(r.getHorse_newDistance()),
                Double.toString(r.getJockey_winPer()),
                Integer.toString(r.getJockey_winCount()),
                Integer.toString(r.getHorse_last4SpeedRate()),
                Integer.toString(r.getHorse_latestSpeedRate()),
                Integer.toString(r.getDays_from_lastRace())
                ));
        writer.write("\r\n");
    }
    private void printNewRace(FileWriter writer, RaceCardItem r) throws IOException {
        writer.write(String.join(",",Integer.toString(r.getRaceSeqOfDay()), r.getRacePlace(),r.getHorseNo(),r.getHorseId(),r.getHorseName(),r.getRaceDate(),
                Integer.toString(r.getDraw()), Integer.toString(r.getDistance()),r.getCourse(),
                Integer.toString(r.getRaceClass()),
                Integer.toString(r.getRating()),r.getJockey(),r.getGoing(),
                Integer.toString(r.getAddedWeight()),
                Integer.toString(r.getDeclaredHorseWeight()),
                Double.toString(r.getHorse_winPer()),
                Integer.toString(r.getHorse_winCount()),
                Integer.toString(r.getHorse_newHorse()),
                Integer.toString(r.getHorse_newDistance()),
                Double.toString(r.getJockey_winPer()),
                Integer.toString(r.getJockey_winCount()),
                Integer.toString(r.getHorse_last4SpeedRate()),
                Integer.toString(r.getHorse_latestSpeedRate()),
                Integer.toString(r.getDays_from_lastRace())));
        writer.write("\r\n");
    }

    private void printHeader(FileWriter writer) throws IOException{
        writer.write("horseNo,horseName,rateDate,draw,standardFinishTime,finishTime," +
                "place,lbw,winOdds,distance,course,raceClass,rating,ratingDelta,jockey,going");
        writer.write("\r\n");

    }
    private void printAnalysis(FileWriter writer, RaceCardAnalysis r) throws IOException {
        writer.write(String.join(",",r.getHorseNo(),r.getHorseName(),r.getRaceDate(),
                Integer.toString(r.getDraw()),Double.toString(r.getStandardFinishTime()),
                Double.toString(r.getFinishTime()),
                Integer.toString(r.getPlace()),Double.toString(r.getLbw()),Double.toString(r.getWinOdds()),
                Integer.toString(r.getDistance()),r.getCourse(),Integer.toString(r.getRaceClass()),
                Integer.toString(r.getRating()),Integer.toString(r.getRatingDelta()),r.getJockey(),r.getGoing()));
        writer.write("\r\n");
    }


    private void printAnalysis_DifferentCourse_DifferentDistance(FileWriter writer,RaceCardAnalysis r)
             throws IOException{
        writer.write(String.join(",",r.getHorseNo(),r.getHorseName(),r.getRaceDate(),
                Integer.toString(r.getDraw()),Double.toString(r.getStandardFinishTime()),
                Double.toString(r.getFinishTime()),
                Integer.toString(r.getPlace()),Double.toString(r.getLbw()),Double.toString(r.getWinOdds()),
                Integer.toString(r.getDistance()),r.getCourse(), Integer.toString(r.getRaceClass()),
                Integer.toString(r.getRating()),Integer.toString(r.getRatingDelta()),r.getJockey(),r.getGoing()));
        writer.write("\r\n");
    }

}
