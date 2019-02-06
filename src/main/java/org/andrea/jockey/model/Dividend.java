package org.andrea.jockey.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Dividend {
    String raceDate;//YYYYMMDD
    int raceSeqOfDay;
    String pool;
    String winning;
    BigDecimal dividend;

    public Dividend(String raceDate, int raceSeqOfDay) {
        this.raceDate = raceDate;
        this.raceSeqOfDay = raceSeqOfDay;
    }

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public int getRaceSeqOfDay() {
        return raceSeqOfDay;
    }

    public void setRaceSeqOfDay(int raceSeqOfDay) {
        this.raceSeqOfDay = raceSeqOfDay;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public String getWinning() {
        return winning;
    }

    public void setWinning(String winning) {
        this.winning = winning;
    }

    public BigDecimal getDividend() {
        return dividend;
    }

    public void setDividend(BigDecimal dividend) {
        this.dividend = dividend;
    }

    @Override
    public String toString() {
        return "Dividend{" +
                "pool='" + pool + '\'' +
                ", winning='" + winning + '\'' +
                ", dividend=" + dividend +
                '}';
    }
}
