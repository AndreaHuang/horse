package org.andrea.jockey.model;

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



}

