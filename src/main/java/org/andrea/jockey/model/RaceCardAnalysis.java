package org.andrea.jockey.model;

public class RaceCardAnalysis extends RaceCardResult {

    private int daysFromLastRace;
    private double standardFinishTime;

    public int getDaysFromLastRace() {
        return daysFromLastRace;
    }

    public void setDaysFromLastRace(int daysFromLastRace) {
        this.daysFromLastRace = daysFromLastRace;
    }

    public double getStandardFinishTime() {
        return standardFinishTime;
    }

    public void setStandardFinishTime(double standardFinishTime) {
        this.standardFinishTime = standardFinishTime;
    }
}
