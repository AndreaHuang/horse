package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.RaceCardAnalysis;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RaceCardRowMapper implements RowMapper<RaceCardResult> {
    @Override
    public RaceCardResult mapRow(ResultSet resultSet, int i) throws SQLException {
        RaceCardResult r = new RaceCardResult();

        r.setRaceDate(resultSet.getString("raceDate"));
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
        r.setJockey(resultSet.getString("jockey"));
        r.setTrainer(resultSet.getString("trainer"));
        r.setAddedWeight(resultSet.getInt("addedWeight"));
        r.setDeclaredHorseWeight(resultSet.getInt("declaredHorseWeight"));
        r.setLbw(resultSet.getDouble("lbs"));
        r.setWinOdds(resultSet.getDouble("winOdds"));
        r.setPlace(resultSet.getInt("place"));
        r.setRunningPosition(resultSet.getString("runningPosition"));
        r.setFinishTime(resultSet.getDouble("finishTime"));
        r.setRating(resultSet.getInt("rating"));


        return r;
    }
}
