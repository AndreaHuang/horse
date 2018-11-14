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
        this.rateDate = info.getRateDate();
        this.going = info.getGoing();
        this.course=info.getCourse();
        this.distance = info.getDistance();
    }

    @Override
    public String toString() {
        return "RaceCardResult{" +
                "place=" + place +
                ", runningPosition='" + runningPosition + '\'' +
                ", finishTimeString='" + finishTimeString + '\'' +
                ", finishTime=" + finishTime +
                ", comment='" + comment + '\'' +
                ", draw=" + draw +
                ", horseNo='" + horseNo + '\'' +
                ", horseId='" + horseId + '\'' +
                ", horseName='" + horseName + '\'' +
                ", jockey='" + jockey + '\'' +
                ", trainer='" + trainer + '\'' +
                ", rating=" + rating +
                ", addedWeight=" + addedWeight +
                ", declaredHorseWeight=" + declaredHorseWeight +
                ", lbwString='" + lbwString + '\'' +
                ", lbw=" + lbw +
                ", winOdds=" + winOdds +
                ", rateDate='" + rateDate + '\'' +
                ", racePlace='" + racePlace + '\'' +
                ", raceId='" + raceId + '\'' +
                ", raceSeqOfDay=" + raceSeqOfDay +
                ", raceClass=" + raceClass +
                ", distance=" + distance +
                ", going='" + going + '\'' +
                ", course='" + course + '\'' +
                '}';
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
