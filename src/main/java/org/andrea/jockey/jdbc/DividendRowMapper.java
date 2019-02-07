package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.Dividend;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DividendRowMapper implements RowMapper<Dividend> {
    @Override
    public Dividend mapRow(ResultSet resultSet, int i) throws SQLException {
        Dividend div = new Dividend();
        div.setRaceDate(resultSet.getString("raceDate"));
        div.setRaceSeqOfDay(resultSet.getInt("raceSeqOfDay"));
        div.setPool(resultSet.getString("pool"));
        div.setWinning(resultSet.getString("winning"));
        div.setDividend(resultSet.getBigDecimal("dividend"));
        return div;
    }
}
