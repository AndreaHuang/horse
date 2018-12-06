package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.RaceCardAnalysis;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RaceCardAnalysisRowMapper implements RowMapper<org.andrea.jockey.model.RaceCardAnalysis> {
    @Override
    public org.andrea.jockey.model.RaceCardAnalysis mapRow(ResultSet resultSet, int i) throws SQLException {
        org.andrea.jockey.model.RaceCardAnalysis r = new org.andrea.jockey.model.RaceCardAnalysis();

        r.setRaceDate(resultSet.getString("rateDate"));
        r.setRacePlace(resultSet.getString("raceMeeting"));
        r.setRaceId(resultSet.getString("raceId"));
        r.setRaceSeqOfDay(resultSet.getInt("raceSeqOfDay"));
        r.setRaceClass(resultSet.getInt("raceClass"));
        r.setDistance(resultSet.getInt("distance"));
        r.setGoing(resultSet.getString("going"));
        r.setCourse(resultSet.getString("course"));
        r.setDraw(resultSet.getInt("draw"));
        r.setHorseId(resultSet.getString("horseId"));
        r.setHorseName(resultSet.getString("horseName"));
        r.setHorseNo(resultSet.getString("newHorseNo"));
        r.setJockey(resultSet.getString("jockey"));
        r.setTrainer(resultSet.getString("trainer"));
        r.setAddedWeight(resultSet.getInt("addedWeight"));
        r.setDeclaredHorseWeight(resultSet.getInt("declaredHorseWeight"));
        r.setLbw(resultSet.getDouble("lbs"));
        r.setWinOdds(resultSet.getDouble("winOdds"));
        r.setPlace(resultSet.getInt("place"));
        r.setRunningPosition(resultSet.getString("runningPosition"));
        r.setFinishTime(resultSet.getDouble("finishTime"));
        r.setRating(resultSet.getInt("newRating"));
        r.setRatingDelta(resultSet.getInt("ratingDelta"));

        return r;
    }
}
