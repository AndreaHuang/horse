package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.HorseStatistics;
import org.andrea.jockey.model.JockeyStatistics;
import org.andrea.jockey.model.RaceStatistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsRowMapper {

    static class HorseRowMapper implements RowMapper<HorseStatistics> {
        @Override
        public HorseStatistics mapRow(ResultSet resultSet, int i) throws SQLException {
            HorseStatistics r = new HorseStatistics();

            r.setRaceDate(resultSet.getString("raceDate"));

            r.setDistance(resultSet.getInt("distance"));

            r.setCourse(resultSet.getString("course"));

            r.setPlace(resultSet.getInt("place"));
            return r;
        }
    }
    static class JockeyRowMapper implements RowMapper<JockeyStatistics> {
        @Override
        public JockeyStatistics mapRow(ResultSet resultSet, int i) throws SQLException {
            JockeyStatistics r = new JockeyStatistics();

            r.setRaceDate(resultSet.getString("raceDate"));

            r.setPlace(resultSet.getInt("place"));
            return r;
        }
    }
    static class RaceRowMapper implements RowMapper<RaceStatistics> {
        @Override
        public RaceStatistics mapRow(ResultSet resultSet, int i) throws SQLException {
            RaceStatistics result = new RaceStatistics();
            result.setAvgFinishTime(resultSet.getDouble("avgFinishTime"));
            result.setMinFinishTime(resultSet.getDouble("minFinishTime"));
            result.setDistance(resultSet.getInt("distance"));
            result.setRaceClass(resultSet.getInt("raceClass"));
            result.setCourse(resultSet.getString("course"));
            result.setGoing(resultSet.getString("going"));
            result.setCount(resultSet.getInt("count"));
            return result;
        }
    }
}
