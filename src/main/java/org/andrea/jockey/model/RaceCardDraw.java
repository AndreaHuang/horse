package org.andrea.jockey.model;

public class RaceCardDraw {

    String racemeeting;
    int distance;
    String course;
    int draw;
    int ttlCount;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    int posCount;
    double fx;

    public String getRacemeeting() {
        return racemeeting;
    }

    public void setRacemeeting(String racemeeting) {
        this.racemeeting = racemeeting;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


    public double getFx() {
        return fx;
    }

    public void setFx(double fx) {
        this.fx = fx;
    }

    public int getTtlCount() {
        return ttlCount;
    }

    public void setTtlCount(int ttlCount) {
        this.ttlCount = ttlCount;
    }

    public int getPosCount() {
        return posCount;
    }

    public void setPosCount(int posCount) {
        this.posCount = posCount;
    }

    @Override
    public String toString() {
        return "RaceCardDraw{" +
                "racemeeting='" + racemeeting + '\'' +
                ", distance=" + distance +
                ", course='" + course + '\'' +
                ", draw=" + draw +
                ", ttlCount=" + ttlCount +
                ", posCount=" + posCount +
                ", fx=" + fx +
                '}';
    }
}
