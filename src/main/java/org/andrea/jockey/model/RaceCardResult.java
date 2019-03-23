package org.andrea.jockey.model;

import lombok.Data;

@Data
public class RaceCardResult extends RaceCardItem {

    private int place;
    private String lbwString;
    private double lbw;
    private String runningPosition;
    private String finishTimeString;
    private double finishTime;
    private String comment;

    public RaceCardResult(){

    }
    public RaceCardResult(RaceInfo info){
        this.raceClass = info.getRaceClass();
        this.raceId =info.getRaceId();
        this.racePlace = info.getRacePlace();
        this.raceSeqOfDay = info.getRaceSeqOfDay();
        this.raceDate = info.getRaceDate();
        this.going = info.getGoing();
        this.course=info.getCourse();
        this.distance = info.getDistance();
    }

    @Override
    public String toString() {
        return super.toString()+
                "place=" + place +
                ", lbwString='" + lbwString + '\'' +
                ", lbw=" + lbw +
                ", runningPosition='" + runningPosition + '\'' +
                ", finishTimeString='" + finishTimeString + '\'' +
                ", finishTime=" + finishTime +
                ", comment='" + comment + '\'' ;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getRunningPosition() {
        return runningPosition;
    }

    public void setRunningPosition(String runningPosition) {
        this.runningPosition = runningPosition;
    }

    public String getFinishTimeString() {
        return finishTimeString;
    }

    public void setFinishTimeString(String finishTimeString) {
        this.finishTimeString = finishTimeString;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getLbwString() {
        return lbwString;
    }


    public void setLbwString(String lbwString) {
        this.lbwString = lbwString;
    }


    public double getLbw() {
        return lbw;
    }


    public void setLbw(double lbw) {
        this.lbw = lbw;
    }
}
