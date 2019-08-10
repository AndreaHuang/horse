package org.andrea.jockey.jdbc;

import org.andrea.jockey.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
            r.setGoing(resultSet.getString("going"));
            r.setFinishTime(resultSet.getDouble("finishTime"));
            r.setRaceClass(resultSet.getInt("raceClass"));
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
            result.setAvgFinishTime(resultSet.getDouble("avg_FinishTime"));
            result.setMinFinishTime(resultSet.getDouble("min_FinishTime"));
            result.setDistance(resultSet.getInt("distance"));
            result.setRaceClass(resultSet.getInt("raceClass"));
            result.setCourse(resultSet.getString("course"));
            result.setGoing(resultSet.getString("going"));
            result.setCount(resultSet.getInt("count"));
            return result;
        }
    }

    static class SurvivalAnalysis_Horse_RowMapper implements RowMapper<SurvivalAnalysis.SurvivalAnalysis_Horse> {
        @Override
        public SurvivalAnalysis.SurvivalAnalysis_Horse mapRow(ResultSet resultSet, int i) throws SQLException {
            SurvivalAnalysis.SurvivalAnalysis_Horse r = new SurvivalAnalysis.SurvivalAnalysis_Horse();

            r.setRaceDate(resultSet.getString("raceDate"));

            r.setRacePlace(resultSet.getString("raceMeeting"));

            r.setDistance(resultSet.getInt("distance"));

            r.setCourse(resultSet.getString("course"));

            r.setFinishTime(resultSet.getDouble("finishTime"));

            r.setHorseId(resultSet.getString("horseId"));


            return r;
        }
    }
    static class SurvivalAnalysis_Draw_RowMapper implements RowMapper<SurvivalAnalysis.SurvivalAnalysis_Draw> {
        @Override
        public SurvivalAnalysis.SurvivalAnalysis_Draw mapRow(ResultSet resultSet, int i) throws SQLException {
            SurvivalAnalysis.SurvivalAnalysis_Draw r = new SurvivalAnalysis.SurvivalAnalysis_Draw();

            r.setRaceDate(resultSet.getString("raceDate"));

            r.setRacePlace(resultSet.getString("raceMeeting"));

            r.setDistance(resultSet.getInt("distance"));

            r.setCourse(resultSet.getString("course"));

            r.setFinishTime(resultSet.getDouble("finishTime"));

            r.setDraw(resultSet.getInt("draw"));


            return r;
        }
    }
    static class SurvivalAnalysis_Jockey_RowMapper implements RowMapper<SurvivalAnalysis.SurvivalAnalysis_Jockey> {
        @Override
        public SurvivalAnalysis.SurvivalAnalysis_Jockey mapRow(ResultSet resultSet, int i) throws SQLException {
            SurvivalAnalysis.SurvivalAnalysis_Jockey r = new SurvivalAnalysis.SurvivalAnalysis_Jockey();

            r.setRaceDate(resultSet.getString("raceDate"));

            r.setRacePlace(resultSet.getString("raceMeeting"));

            r.setDistance(resultSet.getInt("distance"));

            r.setCourse(resultSet.getString("course"));

            r.setFinishTime(resultSet.getDouble("finishTime"));

            r.setJockey(resultSet.getString("jockey"));


            return r;
        }
    }

    static class SurvivalAnalysisRowMapper implements RowMapper<RaceCardResult> {
        @Override
        public RaceCardResult mapRow(ResultSet resultSet, int i) throws SQLException {
            RaceCardResult r = new RaceCardResult();

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
            r.setFinishTime(resultSet.getFloat("finishTime"));

            try {
                r.setPlace(resultSet.getInt("place"));
            } catch(SQLException e){
                r.setPlace(0);
            }

            return r;
        }
    }
    static class SurvivalAnalysisResultRowMapper implements RowMapper<SurvivalAnalysis.SurvivalAnalysis_Result> {
        @Override
        public SurvivalAnalysis.SurvivalAnalysis_Result mapRow(ResultSet resultSet, int i) throws SQLException {
            SurvivalAnalysis.SurvivalAnalysis_Result r = new SurvivalAnalysis.SurvivalAnalysis_Result();


            r.setRaceDate(resultSet.getString("raceDate"));
            r.setRaceMeeting(resultSet.getString("raceMeeting"));

            r.setRaceSeqOfDay(resultSet.getInt("seq"));
            r.setRaceClass(resultSet.getInt("raceClass"));
            r.setDistance(resultSet.getInt("distance"));

            r.setCourse(resultSet.getString("course"));
            r.setDrawA(resultSet.getInt("drawA"));
            r.setDrawB(resultSet.getInt("drawB"));
            r.setHorseIdA(resultSet.getString("horseIdA"));
            r.setHorseIdB(resultSet.getString("horseIdB"));
            r.setHorseNoA(resultSet.getInt("horseNoA"));
            r.setHorseNoB(resultSet.getInt("horseNoB"));
            r.setJockeyA(resultSet.getString("jockeyA"));
            r.setJockeyB(resultSet.getString("jockeyB"));
            r.setFinishTimeA(resultSet.getBigDecimal("finishTimeA"));
            r.setFinishTimeB(resultSet.getBigDecimal("finishTimeB"));
            r.setResult(resultSet.getInt("result"));
            r.setPreditected_result(resultSet.getInt("predicted_result"));
            r.setPreditected_finishTime(resultSet.getBigDecimal("predicted_finishTime"));

            return r;
        }
    }
}
