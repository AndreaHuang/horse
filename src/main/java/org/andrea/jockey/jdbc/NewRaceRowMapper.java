package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.RaceCardAnalysis;
import org.andrea.jockey.model.RaceCardItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewRaceRowMapper implements RowMapper<RaceCardItem> {
    @Override
    public RaceCardItem mapRow(ResultSet resultSet, int i) throws SQLException {
        RaceCardItem r = new RaceCardItem();

        r.setRaceDate(resultSet.getString("raceDate"));
        r.setRacePlace(resultSet.getString("raceMeeting"));

        r.setRaceSeqOfDay(resultSet.getInt("raceSeqOfDay"));
        r.setRaceClass(resultSet.getInt("raceClass"));
        r.setDistance(resultSet.getInt("distance"));
        r.setGoing(resultSet.getString("going"));
        r.setCourse(resultSet.getString("course"));
        r.setDraw(resultSet.getInt("draw"));
        r.setHorseId(resultSet.getString("horseId"));
        r.setHorseName(resultSet.getString("horseName"));
        r.setHorseNo(resultSet.getString("horseNo"));
        r.setJockey(resultSet.getString("jockey"));
        r.setTrainer(resultSet.getString("trainer"));
        r.setAddedWeight(resultSet.getInt("addedWeight"));
        r.setDeclaredHorseWeight(resultSet.getInt("declaredHorseWeight"));
        r.setRating(resultSet.getInt("rating"));
        r.setRatingDelta(resultSet.getInt("ratingDelta"));
        r.setHorse_winPer(resultSet.getDouble("horse_winPer"));
        r.setHorse_winCount(resultSet.getInt("horse_winCount"));
        r.setHorse_newDistance(resultSet.getInt("horse_newDistance"));
        r.setHorse_newHorse(resultSet.getInt("horse_newHorse"));
        r.setJockey_winPer(resultSet.getDouble("jockey_winPer"));
        r.setJockey_winCount(resultSet.getInt("jockey_winCount"));
        r.setHorse_last4SpeedRate(resultSet.getInt("horse_last4SpeedRate"));
        r.setHorse_latestSpeedRate(resultSet.getInt("horse_latestSpeedRate"));
        r.setDays_from_lastRace(resultSet.getInt("days_from_lastRace"));
        r.setPredicted_finishTime(resultSet.getDouble("predicted_finishTime"));
        r.setPredicted_place(resultSet.getInt("predicted_place"));

        return r;
    }
}
