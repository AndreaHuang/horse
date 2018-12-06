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

    public int getRatingDelta() {
        return ratingDelta;
    }

    public void setRatingDelta(int ratingDelta) {
        this.ratingDelta = ratingDelta;
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
}
