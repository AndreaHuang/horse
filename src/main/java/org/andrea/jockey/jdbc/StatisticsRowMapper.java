package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.HorseStatistics;
import org.andrea.jockey.model.JockeyStatistics;
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
}
