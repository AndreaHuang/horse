package org.andrea.jockey.model;

import java.math.BigDecimal;
import java.util.Objects;

public class SurvivalAnalysis {

    private String raceDate;//YYYYMMDD
    private String racePlace;
    private int distance;
    private String course;
    private double finishTime;

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }


    public String getRaceDate() {
        return raceDate;
    }


    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }


    public String getRacePlace() {
        return racePlace;
    }


    public void setRacePlace(String racePlace) {
        this.racePlace = racePlace;
    }


    public int getDistance() {
        return distance;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }


    public String getCourse() {
        return course;
    }


    public void setCourse(String course) {
        this.course = course;
    }

    public SurvivalAnalysis_Core toPlaceDistanceCourse(){
        SurvivalAnalysis_Core result = new SurvivalAnalysis_Core();
        result.setCourse(this.getCourse());
        result.setDistance(this.getDistance());
        result.setRacePlace(this.getRacePlace());
        return result;
    }
    public static class SurvivalAnalysis_Core{
        private String racePlace;
        private int distance;
        private String course;

        public String getRacePlace() {
            return racePlace;
        }

        public void setRacePlace(String racePlace) {
            this.racePlace = racePlace;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        @Override
        public String toString() {
//            return "SurvivalAnalysis_Core{" +
//                    "racePlace='" + racePlace + '\'' +
//                    ", distance=" + distance +
//                    ", course='" + course + '\'' +
//                    '}';
            return racePlace +"," + distance+","+course;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SurvivalAnalysis_Core that = (SurvivalAnalysis_Core) o;
            return getDistance() == that.getDistance() &&
                    Objects.equals(getRacePlace(), that.getRacePlace()) &&
                    Objects.equals(getCourse(), that.getCourse());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRacePlace(), getDistance(), getCourse());
        }
    }


    public static class SurvivalAnalysis_Horse_Core{
        private String horseId;
        private double finishTime;
        public String getHorseId() {
            return horseId;
        }

        public void setHorseId(String horseId) {
            this.horseId = horseId;
        }


        public double getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(double finishTime) {
            this.finishTime = finishTime;
        }

    }

    public static class SurvivalAnalysis_Draw_Core{
        private int draw;
        private double finishTime;

        public int getDraw() {
            return draw;
        }

        public void setDraw(int draw) {
            this.draw = draw;
        }

        public double getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(double finishTime) {
            this.finishTime = finishTime;
        }
    }
    public static class SurvivalAnalysis_Jockey_Core{
        private String jockey;
        private double finishTime;

        public String getJockey() {
            return jockey;
        }

        public void setJockey(String jockey) {
            this.jockey = jockey;
        }

        public double getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(double finishTime) {
            this.finishTime = finishTime;
        }
    }
    public static class SurvivalAnalysis_Horse extends SurvivalAnalysis {
        private String horseId;

        public String getHorseId() {
            return horseId;
        }

        public void setHorseId(String horseId) {
            this.horseId = horseId;
        }

        public SurvivalAnalysis_Horse_Core toHorseIdFinishTime(){
            SurvivalAnalysis_Horse_Core result = new SurvivalAnalysis_Horse_Core();
            result.setFinishTime(this.getFinishTime());
            result.setHorseId(this.getHorseId());
            return result;
        }

    }

    public static class SurvivalAnalysis_Draw extends SurvivalAnalysis {
        private int draw;

        public int getDraw() {
            return draw;
        }

        public void setDraw(int draw) {
            this.draw = draw;
        }
        public SurvivalAnalysis_Draw_Core toDrawFinishTime(){
            SurvivalAnalysis_Draw_Core result = new SurvivalAnalysis_Draw_Core();
            result.setFinishTime(this.getFinishTime());
            result.setDraw(this.getDraw());
            return result;
        }
    }

    public static class SurvivalAnalysis_Jockey extends SurvivalAnalysis {
        private String Jockey;

        public String getJockey() {
            return Jockey;
        }

        public void setJockey(String jockey) {
            Jockey = jockey;
        }
        public SurvivalAnalysis_Jockey_Core toJockeyFinishTime(){
            SurvivalAnalysis_Jockey_Core result = new SurvivalAnalysis_Jockey_Core();
            result.setFinishTime(this.getFinishTime());
            result.setJockey(this.getJockey());
            return result;
        }
    }


    public static class SurvivalAnalysis_Result {

        private String raceMeeting;
        private String raceDate;
        private int raceSeqOfDay;//YYYYMMDD
        private int raceClass;
        private int distance;
        private String course;
        private int horseNoA;
        private int horseNoB;
        private String horseIdA;
        private String horseIdB;
        private int drawA;
        private int drawB;
        private String jockeyA;
        private String jockeyB;
        private int jockeyACnt;
        private int jockeyBCnt;
        private BigDecimal jockeyRelRiskA2B;
        private int drawACnt;
        private int drawBCnt;
        private BigDecimal drawRelRiskA2B;
        private int sameHorseACnt;
        private int sameHorseBCnt;
        private BigDecimal sameHorseRelRiskA2B;
        private int diffHorseACnt;
        private int diffHorseBCnt;
        private BigDecimal diffHorseRelRiskA2B;
        private BigDecimal finishTimeA;
        private BigDecimal finishTimeB;
        private int result;
        private BigDecimal preditected_finishTime;
        private int preditected_result;

        public String getRaceMeeting() {
            return raceMeeting;
        }

        public void setRaceMeeting(String raceMeeting) {
            this.raceMeeting = raceMeeting;
        }

        public String getRaceDate() {
            return raceDate;
        }

        public void setRaceDate(String racedate) {
            this.raceDate = racedate;
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

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public int getHorseNoA() {
            return horseNoA;
        }

        public void setHorseNoA(int horseNoA) {
            this.horseNoA = horseNoA;
        }

        public int getHorseNoB() {
            return horseNoB;
        }

        public void setHorseNoB(int horseNoB) {
            this.horseNoB = horseNoB;
        }

        public String getHorseIdA() {
            return horseIdA;
        }

        public void setHorseIdA(String horseIdA) {
            this.horseIdA = horseIdA;
        }

        public String getHorseIdB() {
            return horseIdB;
        }

        public void setHorseIdB(String horseIdB) {
            this.horseIdB = horseIdB;
        }

        public int getDrawA() {
            return drawA;
        }

        public void setDrawA(int drawA) {
            this.drawA = drawA;
        }

        public int getDrawB() {
            return drawB;
        }

        public void setDrawB(int drawB) {
            this.drawB = drawB;
        }

        public String getJockeyA() {
            return jockeyA;
        }

        public void setJockeyA(String jockeyA) {
            this.jockeyA = jockeyA;
        }

        public String getJockeyB() {
            return jockeyB;
        }

        public void setJockeyB(String jockeyB) {
            this.jockeyB = jockeyB;
        }

        public int getJockeyACnt() {
            return jockeyACnt;
        }

        public void setJockeyACnt(int jockeyACnt) {
            this.jockeyACnt = jockeyACnt;
        }

        public int getJockeyBCnt() {
            return jockeyBCnt;
        }

        public void setJockeyBCnt(int jockeyBCnt) {
            this.jockeyBCnt = jockeyBCnt;
        }

        public BigDecimal getJockeyRelRiskA2B() {
            return jockeyRelRiskA2B;
        }

        public void setJockeyRelRiskA2B(BigDecimal jockeyRelRiskA2B) {
            this.jockeyRelRiskA2B = jockeyRelRiskA2B;
        }

        public int getDrawACnt() {
            return drawACnt;
        }

        public void setDrawACnt(int drawACnt) {
            this.drawACnt = drawACnt;
        }

        public int getDrawBCnt() {
            return drawBCnt;
        }

        public void setDrawBCnt(int drawBCnt) {
            this.drawBCnt = drawBCnt;
        }

        public BigDecimal getDrawRelRiskA2B() {
            return drawRelRiskA2B;
        }

        public void setDrawRelRiskA2B(BigDecimal drawRelRiskA2B) {
            this.drawRelRiskA2B = drawRelRiskA2B;
        }

        public int getSameHorseACnt() {
            return sameHorseACnt;
        }

        public void setSameHorseACnt(int sameHorseACnt) {
            this.sameHorseACnt = sameHorseACnt;
        }

        public int getSameHorseBCnt() {
            return sameHorseBCnt;
        }

        public void setSameHorseBCnt(int sameHorseBCnt) {
            this.sameHorseBCnt = sameHorseBCnt;
        }

        public BigDecimal getSameHorseRelRiskA2B() {
            return sameHorseRelRiskA2B;
        }

        public void setSameHorseRelRiskA2B(BigDecimal sameHorseRelRiskA2B) {
            this.sameHorseRelRiskA2B = sameHorseRelRiskA2B;
        }

        public int getDiffHorseACnt() {
            return diffHorseACnt;
        }

        public void setDiffHorseACnt(int diffHorseACnt) {
            this.diffHorseACnt = diffHorseACnt;
        }

        public int getDiffHorseBCnt() {
            return diffHorseBCnt;
        }

        public void setDiffHorseBCnt(int diffHorseBCnt) {
            this.diffHorseBCnt = diffHorseBCnt;
        }

        public BigDecimal getDiffHorseRelRiskA2B() {
            return diffHorseRelRiskA2B;
        }

        public void setDiffHorseRelRiskA2B(BigDecimal diffHorseRelRiskA2B) {
            this.diffHorseRelRiskA2B = diffHorseRelRiskA2B;
        }

        public BigDecimal getFinishTimeA() {
            return finishTimeA;
        }

        public void setFinishTimeA(BigDecimal finishTimeA) {
            this.finishTimeA = finishTimeA;
        }

        public BigDecimal getFinishTimeB() {
            return finishTimeB;
        }

        public void setFinishTimeB(BigDecimal finishTimeB) {
            this.finishTimeB = finishTimeB;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public BigDecimal getPreditected_finishTime() {
            return preditected_finishTime;
        }

        public void setPreditected_finishTime(BigDecimal preditected_finishTime) {
            this.preditected_finishTime = preditected_finishTime;
        }

        public int getPreditected_result() {
            return preditected_result;
        }

        public void setPreditected_result(int preditected_result) {
            this.preditected_result = preditected_result;
        }
    }

}

