package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.RaceCardDraw;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RaceCardDrawRowMapper implements RowMapper<RaceCardDraw> {

    @Override
    public RaceCardDraw mapRow(ResultSet resultSet, int i) throws SQLException {
        RaceCardDraw result = new RaceCardDraw();
        result.setRacemeeting(resultSet.getString("racemeeting"));
        result.setDistance(resultSet.getInt("distance"));
        result.setTtlCount(resultSet.getInt("ttlCount"));
        result.setPosCount(resultSet.getInt("posCount"));
        result.setDraw(resultSet.getInt("draw"));
        return result;
    }
}
