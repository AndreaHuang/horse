package org.andrea.jockey.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class HorseStatistics {
    @Getter @Setter String  raceDate;//YYYYMMDD
    @Getter @Setter int raceClass;
    @Getter @Setter int distance;
    @Getter @Setter int place;
    @Getter @Setter String course;
    @Getter @Setter String going;
    @Getter @Setter double finishTime;

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public int getRaceClass() {
        return raceClass;
    }

    public void setRaceClass(int raceClass) {
        this.raceClass = raceClass;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public String getGoing() {
        return going;
    }

    public void setGoing(String going) {
        this.going = going;
    }
}
