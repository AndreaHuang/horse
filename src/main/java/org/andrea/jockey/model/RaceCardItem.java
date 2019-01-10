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
