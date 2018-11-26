package org.andrea.jockey.predict;

import org.andrea.jockey.jdbc.RecordCardDAO;
import org.andrea.jockey.model.RaceCardAnalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
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

    @Autowired
    RecordCardDAO dao;

    public void predictAll(){
        int raceDate = dao.getNewRaceDate();
        int raceNumber = dao.getNewRaceNumber();
        for(int i=1; i<=raceNumber;i++){
            this.predictARace(raceDate,i);
        }
    }
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

    private void printHeader(FileWriter writer) throws IOException{
        writer.write("horseNo,horseName,rateDate,draw,standardFinishTime,finishTime," +
                "place,lbw,winOdds,rating,ratingDelta\r\n");

    }
    private void printAnalysis(FileWriter writer, RaceCardAnalysis r) throws IOException {
        writer.write((String.join(",",r.getHorseNo(),r.getHorseName(),r.getRateDate(),
                Integer.toString(r.getDraw()),Double.toString(r.getStandardFinishTime()),
                Double.toString(r.getFinishTime()),
                Integer.toString(r.getPlace()),Double.toString(r.getLbw()),Double.toString(r.getWinOdds()),
                Integer.toString(r.getRating()),Integer.toString(r.getRatingDelta()),"\r\n")
               ));
    }


    private void printAnalysis_DifferentCourse_DifferentDistance(FileWriter writer,RaceCardAnalysis r)
             throws IOException{
        writer.write((String.join(",",r.getHorseNo(),r.getHorseName(),r.getRateDate(),
                Integer.toString(r.getDraw()),Double.toString(r.getStandardFinishTime()),
                Double.toString(r.getFinishTime()),
                Integer.toString(r.getPlace()),Double.toString(r.getLbw()),Double.toString(r.getWinOdds()),
                Integer.toString(r.getDistance()),r.getCourse(), Integer.toString(r.getRaceClass()),
                Integer.toString(r.getRating()),Integer.toString(r.getRatingDelta()),"\r\n")));
    }

}
