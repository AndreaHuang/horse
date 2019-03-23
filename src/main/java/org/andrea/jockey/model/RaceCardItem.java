package org.andrea.jockey.model;

import lombok.Data;

@Data
public class RaceCardItem extends RaceInfo{

    int draw;
    String horseNo;
    String horseId;
    String horseName;
    String jockey;
    String trainer;
    int rating;
    int ratingDelta;
    int addedWeight;

    /*Statistic*/
    private double horse_winPer;
    private int horse_winCount;
    private int horse_newDistance; //0 or 1
    private int horse_newHorse;
    private double jockey_winPer;
    private int jockey_winCount;
    private int horse_last4SpeedRate;
    private int horse_latestSpeedRate;
    private int days_from_lastRace;
    private double predicted_finishTime;
    private int predicted_place;

    private double propByWinOdds;
    private int jockeyTtlCnt;
    private int jockeyPosCnt;
    private double jockeyFx;
    private int jockeyTtlCnt_Distance;
    private int jockeyPosCnt_Distance;
    private double jockeyFx_Distance;

    private int horseTtlCnt;
    private int horsePosCnt;
    private double horseFx;
    private int horseTtlCnt_Distance;
    private int horsePosCnt_Distance;
    private double horseFx_Distance;

    @Override
    public String toString() {
        return
                "draw=" + draw +
                ", horseNo='" + horseNo + '\'' +
                ", horseId='" + horseId + '\'' +
                ", horseName='" + horseName + '\'' +
                ", jockey='" + jockey + '\'' +
                ", trainer='" + trainer + '\'' +
                ", rating=" + rating +
                ", ratingDelta=" + ratingDelta +
                ", addedWeight=" + addedWeight +
                ", horse_winPer=" + horse_winPer +
                ", horse_winCount=" + horse_winCount +
                ", horse_newDistance=" + horse_newDistance +
                ", horse_newHorse=" + horse_newHorse +
                ", jockey_winPer=" + jockey_winPer +
                ", jockey_winCount=" + jockey_winCount +
                ", horse_last4SpeedRate=" + horse_last4SpeedRate +
                ", horse_latestSpeedRate=" + horse_latestSpeedRate +
                ", days_from_lastRace=" + days_from_lastRace +
                ", predicted_finishTime=" + predicted_finishTime +
                ", predicted_place=" + predicted_place +
                ", propByWinOdds=" + propByWinOdds +
                ", jockeyTtlCnt=" + jockeyTtlCnt +
                ", jockeyPosCnt=" + jockeyPosCnt +
                ", jockeyFx=" + jockeyFx +
                ", jockeyTtlCnt_Distance=" + jockeyTtlCnt_Distance +
                ", jockeyPosCnt_Distance=" + jockeyPosCnt_Distance +
                ", jockeyFx_Distance=" + jockeyFx_Distance +
                ", horseTtlCnt=" + horseTtlCnt +
                ", horsePosCnt=" + horsePosCnt +
                ", horseFx=" + horseFx +
                ", horseTtlCnt_Distance=" + horseTtlCnt_Distance +
                ", horsePosCnt_Distance=" + horsePosCnt_Distance +
                ", horseFx_Distance=" + horseFx_Distance +
                ", declaredHorseWeight=" + declaredHorseWeight +
                ", winOdds=" + winOdds +
                ", raceDate='" + raceDate + '\'' +
                ", racePlace='" + racePlace + '\'' +
                ", raceId='" + raceId + '\'' +
                ", raceSeqOfDay=" + raceSeqOfDay +
                ", raceClass=" + raceClass +
                ", distance=" + distance +
                ", going='" + going + '\'' +
                ", course='" + course + '\'' ;
    }

    public double getPropByWinOdds() {
        return propByWinOdds;
    }

    public void setPropByWinOdds(double propByWinOdds) {
        this.propByWinOdds = propByWinOdds;
    }

    public int getJockeyTtlCnt() {
        return jockeyTtlCnt;
    }

    public void setJockeyTtlCnt(int jockeyTtlCnt) {
        this.jockeyTtlCnt = jockeyTtlCnt;
    }

    public int getJockeyPosCnt() {
        return jockeyPosCnt;
    }

    public void setJockeyPosCnt(int jockeyPosCnt) {
        this.jockeyPosCnt = jockeyPosCnt;
    }

    public double getJockeyFx() {
        return jockeyFx;
    }

    public void setJockeyFx(double jockeyFx) {
        this.jockeyFx = jockeyFx;
    }

    public int getJockeyTtlCnt_Distance() {
        return jockeyTtlCnt_Distance;
    }

    public void setJockeyTtlCnt_Distance(int jockeyTtlCnt_Distance) {
        this.jockeyTtlCnt_Distance = jockeyTtlCnt_Distance;
    }

    public int getJockeyPosCnt_Distance() {
        return jockeyPosCnt_Distance;
    }

    public void setJockeyPosCnt_Distance(int jockeyPosCnt_Distance) {
        this.jockeyPosCnt_Distance = jockeyPosCnt_Distance;
    }

    public double getJockeyFx_Distance() {
        return jockeyFx_Distance;
    }

    public void setJockeyFx_Distance(double jockeyFx_Distance) {
        this.jockeyFx_Distance = jockeyFx_Distance;
    }

    public int getHorseTtlCnt() {
        return horseTtlCnt;
    }

    public void setHorseTtlCnt(int horseTtlCnt) {
        this.horseTtlCnt = horseTtlCnt;
    }

    public int getHorsePosCnt() {
        return horsePosCnt;
    }

    public void setHorsePosCnt(int horsePosCnt) {
        this.horsePosCnt = horsePosCnt;
    }

    public double getHorseFx() {
        return horseFx;
    }

    public void setHorseFx(double horseFx) {
        this.horseFx = horseFx;
    }

    public int getHorseTtlCnt_Distance() {
        return horseTtlCnt_Distance;
    }

    public void setHorseTtlCnt_Distance(int horseTtlCnt_Distance) {
        this.horseTtlCnt_Distance = horseTtlCnt_Distance;
    }

    public int getHorsePosCnt_Distance() {
        return horsePosCnt_Distance;
    }

    public void setHorsePosCnt_Distance(int horsePosCnt_Distance) {
        this.horsePosCnt_Distance = horsePosCnt_Distance;
    }

    public double getHorseFx_Distance() {
        return horseFx_Distance;
    }

    public void setHorseFx_Distance(double horseFx_Distance) {
        this.horseFx_Distance = horseFx_Distance;
    }

    public double getPredicted_finishTime() {
        return predicted_finishTime;
    }

    public void setPredicted_finishTime(double predicted_finishTime) {
        this.predicted_finishTime = predicted_finishTime;
    }

    public int getPredicted_place() {
        return predicted_place;
    }

    public void setPredicted_place(int predicted_place) {
        this.predicted_place = predicted_place;
    }

    public int getDays_from_lastRace() {
        return days_from_lastRace;
    }

    public void setDays_from_lastRace(int days_from_lastRace) {
        this.days_from_lastRace = days_from_lastRace;
    }

    public int getHorse_last4SpeedRate() {
        return horse_last4SpeedRate;
    }

    public void setHorse_last4SpeedRate(int horse_last4SpeedRate) {
        this.horse_last4SpeedRate = horse_last4SpeedRate;
    }

    public int getHorse_latestSpeedRate() {
        return horse_latestSpeedRate;
    }

    public void setHorse_latestSpeedRate(int horse_latestSpeedRate) {
        this.horse_latestSpeedRate = horse_latestSpeedRate;
    }

    public int getRatingDelta() {
        return ratingDelta;
    }

    public void setRatingDelta(int ratingDelta) {
        this.ratingDelta = ratingDelta;
    }

    public int getHorse_newHorse() {
        return horse_newHorse;
    }

    public void setHorse_newHorse(int horse_newHorse) {
        this.horse_newHorse = horse_newHorse;
    }

    int declaredHorseWeight;


    double winOdds;

    public RaceCardItem(RaceInfo info){
        this.raceClass = info.getRaceClass();
        this.raceId =info.getRaceId();
        this.racePlace = info.getRacePlace();
        this.raceSeqOfDay = info.getRaceSeqOfDay();
        this.raceDate = info.getRaceDate();
        this.going = info.getGoing();
        this.course=info.getCourse();
        this.distance = info.getDistance();
    }

    public RaceCardItem(){

    }
    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public String getHorseNo() {
        return horseNo;
    }

    public void setHorseNo(String horseNo) {
        this.horseNo = horseNo;
    }

    public String getHorseId() {
        return horseId;
    }

    public void setHorseId(String horseId) {
        this.horseId = horseId;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getJockey() {
        return jockey;
    }

    public void setJockey(String jockey) {
        this.jockey = jockey;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getAddedWeight() {
        return addedWeight;
    }

    public void setAddedWeight(int addedWeight) {
        this.addedWeight = addedWeight;
    }

    public int getDeclaredHorseWeight() {
        return declaredHorseWeight;
    }

    public void setDeclaredHorseWeight(int declaredHorseWeight) {
        this.declaredHorseWeight = declaredHorseWeight;
    }


    public double getWinOdds() {
        return winOdds;
    }

    public void setWinOdds(double winOdds) {
        this.winOdds = winOdds;
    }

    public double getHorse_winPer() {
        return horse_winPer;
    }

    public void setHorse_winPer(double horse_winPer) {
        this.horse_winPer = horse_winPer;
    }

    public int getHorse_winCount() {
        return horse_winCount;
    }

    public void setHorse_winCount(int horse_winCount) {
        this.horse_winCount = horse_winCount;
    }

    public int getHorse_newDistance() {
        return horse_newDistance;
    }

    public void setHorse_newDistance(int horse_newDistance) {
        this.horse_newDistance = horse_newDistance;
    }

    public double getJockey_winPer() {
        return jockey_winPer;
    }

    public void setJockey_winPer(double jockey_winPer) {
        this.jockey_winPer = jockey_winPer;
    }

    public int getJockey_winCount() {
        return jockey_winCount;
    }

    public void setJockey_winCount(int jockey_winCount) {
        this.jockey_winCount = jockey_winCount;
    }


    public String printStatisticsResult() {
        return "" +
                raceDate  +
                "," + raceSeqOfDay +
                "," + raceClass +
                "," + distance +
                "," + course +
                "," + horseNo +
                "," + horseId +
                "," + horse_newHorse+
                ","+ horse_winPer +
                "," + jockey_winPer +
                "," + horse_newDistance +
                "," + horse_winCount +
                "," + jockey_winCount +
                "," + jockey +
                "," + addedWeight +
                "," + declaredHorseWeight +
                "," + winOdds ;
    }
    public static String printStatisticsHeader() {
        return
                "raceDate" +
                ", raceSeqOfDay"+
                ", raceClass"+
                ", distance,"+
                ", course,"+
                ", horseNo,"+
                ", horseId,"+
                ", horse_newHorse,"+
                ", horse_winPer,"+
                ", jockey_winPer,"+
                ", horse_newDistance,"+
                ", horse_winCount,"+
                ", jockey_winCount,"+
                ", jockey,"+
                ", addedWeight,"+
                ", declaredHorseWeight,"+
                ", winOdds";
    }
}
