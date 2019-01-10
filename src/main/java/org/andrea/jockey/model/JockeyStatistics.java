package org.andrea.jockey.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class JockeyStatistics {
    @Getter @Setter String  raceDate;//YYYYMMDD
    @Getter @Setter int place;

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
