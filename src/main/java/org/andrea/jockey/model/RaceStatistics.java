package org.andrea.jockey.model;

import lombok.Data;

@Data
public class RaceStatistics {
    private int distance;
    private int raceClass;
    private double avgFinishTime;
    private double minFinishTime;
    private String going;
    private String course;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRaceClass() {
        return raceClass;
    }

    public void setRaceClass(int raceClass) {
        this.raceClass = raceClass;
    }

    public double getAvgFinishTime() {
        return avgFinishTime;
    }

    public void setAvgFinishTime(double avgFinishTime) {
        this.avgFinishTime = avgFinishTime;
    }

    public double getMinFinishTime() {
        return minFinishTime;
    }

    public void setMinFinishTime(double minFinishTime) {
        this.minFinishTime = minFinishTime;
    }

    public String getGoing() {
        return going;
    }

    public void setGoing(String going) {
        this.going = going;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
