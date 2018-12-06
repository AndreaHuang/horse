package org.andrea.jockey.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
public class RaceInfo {

    @Getter @Setter String  raceDate;//YYYYMMDD
    @Getter @Setter String racePlace;
    @Getter @Setter String raceId;
    @Getter @Setter int raceSeqOfDay;
    @Getter @Setter int raceClass;
    @Getter @Setter int distance;
    @Getter @Setter String going;
    @Getter @Setter String course;

    public String getRacePlace() {
        return racePlace;
    }

    public void setRacePlace(String racePlace) {
        this.racePlace = racePlace;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public int getRaceSeqOfDay() {
        return raceSeqOfDay;
    }

    public void setRaceSeqOfDay(int raceSeqOfDay) {
        this.raceSeqOfDay = raceSeqOfDay;
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

    @Override
    public String toString() {
        return "RaceInfo{" +
                "rateDate='" + raceDate + '\'' +
                ", racePlace='" + racePlace + '\'' +
                ", raceId='" + raceId + '\'' +
                ", raceSeqOfDay=" + raceSeqOfDay +
                ", raceClass=" + raceClass +
                ", distance=" + distance +
                ", going='" + going + '\'' +
                ", course='" + course + '\'' +
                '}';
    }

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String rateDate) {
        this.raceDate = rateDate;
    }
}
