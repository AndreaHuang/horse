package org.andrea.jockey.jdbc;



import org.andrea.jockey.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;



import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RecordCardDAO {

    private JdbcTemplate jdbc;
    @Autowired
    public RecordCardDAO(DataSource ds){
        this.jdbc=new JdbcTemplate(ds);
    }


    private final String SQL_INSERT="INSERT INTO racecard " +
            "(raceDate," +
            "raceMeeting," +
            "raceId," +
            "raceSeqOfDay," +
            "raceClass," +
            "distance," +
            "going," +
            "course," +
            "draw," +
            "horseId," +
            "horseName," +
            "horseNo," +
            "rating," +
            "jockey," +
            "trainer," +
            "addedWeight," +
            "declaredHorseWeight," +
            "lbsString," +
            "lbs," +
            "winOdds," +
            "place," +
            "runningPosition," +
            "finishTimeString," +
            "finishTime," +
            "cmment)" +
            "VALUES" +
            "(?,"+//<{1: rateDate: }>," +
            "?,"+//<{2: raceMeeting: }>," +
            "?,"+//<{3: raceId: }>," +
            "?,"+//<{4: raceSeqOfDay: }>," +
            "?,"+//<{5: raceClass: }>," +
            "?,"+//<{6: distance: }>," +
            "?,"+//<{7: going: }>," +
            "?,"+//<{8: course: }>," +
            "?,"+//<{9: draw: }>," +
            "?,"+//<{10: horseId: }>," +
            "?,"+//<{11: horseName: }>," +
            "?,"+//<{12: hourseNo: }>," +
            "?,"+//<{13: rating: }>," +
            "?,"+//<{14:jockey: }>," +
            "?,"+//<{15:trainer: }>," +
            "?,"+//<{16:addedWeight: }>," +
            "?,"+//<{17:declaredHorseWeight: }>," +
            "?,"+//<{18:lbsString: }>," +
            "?,"+//<{19:lbs: }>," +
            "?,"+//<{20:winOdds: }>," +
            "?,"+//<{21:place: }>," +
            "?,"+//<{22:runningPosition: }>," +
            "?,"+//<{23:finishTimeString: }>," +
            "?,"+//<{24:finishTime: }>," +
            "?)";//<{25:cmment: }>);" +

    private final String SQL_INSERTNEWRECORD="INSERT INTO newrace " +
            "(raceDate," +
            "raceMeeting," +

            "raceSeqOfDay," +
            "raceClass," +
            "distance," +
            "going," +
            "course," +
            "draw," +
            "horseId," +
            "horseName," +
            "horseNo," +
            "rating," +

            "jockey," +
            "trainer," +
            "addedWeight," +
            "declaredHorseWeight," +
          //  "lbsString," +
         //   "lbs," +
         //   "winOdds," +
         //   "place," +
         //   "runningPosition," +
         //   "finishTimeString," +
         //   "finishTime," +
          //  "cmment
            "ratingDelta"+
            ")" +
            "VALUES" +
            "(?,"+//<{1: rateDate: }>," +
            "?,"+//<{2: raceMeeting: }>," +

            "?,"+//<{4: raceSeqOfDay: }>," +
            "?,"+//<{5: raceClass: }>," +
            "?,"+//<{6: distance: }>," +
            "?,"+//<{7: going: }>," +
            "?,"+//<{8: course: }>," +
            "?,"+//<{9: draw: }>," +
            "?,"+//<{10: horseId: }>," +
            "?,"+//<{11: horseName: }>," +
            "?,"+//<{12: hourseNo: }>," +
            "?,"+//<{13: rating: }>," +
            "?,"+//<{14:jockey: }>," +
            "?,"+//<{15:trainer: }>," +
            "?,"+//<{16:addedWeight: }>," +
            "?,"+//<{17:declaredHorseWeight: }>," +
           // "?,"+//<{18:lbsString: }>," +
           // "?,"+//<{19:lbs: }>," +
           // "?,"+//<{20:winOdds: }>," +
           // "?,"+//<{21:place: }>," +
           // "?,"+//<{22:runningPosition: }>," +
           // "?,"+//<{23:finishTimeString: }>," +
           // "?,"+//<{24:finishTime: }>," +
           // "?//<{25:cmment: }>);" +
            "?"+//<{ratingDelta }>," +
            ")";

    private static final String SQL_INSERT_RACE_STATISTICS="INSERT INTO `jockey`.`racestats`\n" +
            "(raceClass," +
            "distance," +
            "going," +
            "course," +
            "avg_finishTime," +
            "min_finishTime," +
            "count) " +
            "VALUES " +
            "(?," +
            "?," +
            "?," +
            "?," +
            "?," +
            "?," +
            "?);";
    private static final String SQL_UPDATE_RACECARD_STATISTIC="update racecard set "+
            " horse_winPer = ?, horse_winCount=?,horse_newDistance=?, " +
            " horse_newHorse =?,jockey_winPer=?,jockey_winCount=?," +
            " horse_last4SpeedRate =?, horse_latestSpeedRate =? , days_from_lastRace=? "+
            " where raceDate = ?  and RaceSeqOfDay =? and horseId =?";

    private static final String SQL_UPDATE_NEWRACE_STATISTIC="update newrace set "+
            " horse_winPer = ?, horse_winCount=?,horse_newDistance=?, " +
            " horse_newHorse =?,jockey_winPer=?,jockey_winCount=?," +
            " horse_last4SpeedRate =?, horse_latestSpeedRate =? , days_from_lastRace=? "+
            " where raceDate = ?  and RaceSeqOfDay =? and horseId =?";


    public void batchInsertResults(final List<RaceCardResult> raceResultList){

        this.jdbc.batchUpdate(SQL_INSERT,
                new BatchPreparedStatementSetter(){

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        RaceCardResult race = raceResultList.get(i);
                        //System.out.println(race);
                        ps.setString(1,race.getRaceDate());
                        ps.setString(2,race.getRacePlace());
                        ps.setString(3, race.getRaceId());
                        ps.setInt(4,race.getRaceSeqOfDay());
                        ps.setInt(5,race.getRaceClass());
                        ps.setInt(6,race.getDistance());
                        ps.setString(7,race.getGoing());
                        ps.setString(8,race.getCourse());
                        ps.setInt(9,race.getDraw());
                        ps.setString(10,race.getHorseId());
                        ps.setString(11,race.getHorseName());
                        ps.setString(12,race.getHorseNo());
                        ps.setInt(13,race.getRating());
                        ps.setString(14,race.getJockey());
                        ps.setString(15,race.getTrainer());
                        ps.setInt(16,race.getAddedWeight());
                        ps.setInt(17,race.getDeclaredHorseWeight());
                        ps.setString(18,race.getLbwString());
                        ps.setDouble(19,race.getLbw());
                        ps.setDouble(20,race.getWinOdds());
                        ps.setInt(21,race.getPlace());
                        ps.setString(22,race.getRunningPosition());
                        ps.setString(23,race.getFinishTimeString());
                        ps.setDouble(24,race.getFinishTime());
                        ps.setString(25,race.getComment());
                    }

                    public int getBatchSize() {
                        return raceResultList.size();
                    }
                });
    }

    public void batchInsertRaceStates(final List<RaceStatistics> raceStatisticsList){

        this.jdbc.batchUpdate(SQL_INSERT_RACE_STATISTICS,
                new BatchPreparedStatementSetter(){

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        RaceStatistics statistics = raceStatisticsList.get(i);
                        //System.out.println(race);
                        int idx =0;
                        ps.setInt(++idx,statistics.getRaceClass());
                        ps.setInt(++idx,statistics.getDistance());

                        ps.setString(++idx,statistics.getGoing());

                        ps.setString(++idx,statistics.getCourse());
                        ps.setDouble(++idx,statistics.getAvgFinishTime());
                        ps.setDouble(++idx,statistics.getMinFinishTime());
                        ps.setInt(++idx,statistics.getCount());

                    }

                    public int getBatchSize() {
                        return raceStatisticsList.size();
                    }
                });
    }

    public void batchInsertNewRace(final List<RaceCardItem> raceCardList){

        this.jdbc.batchUpdate(SQL_INSERTNEWRECORD,
                new BatchPreparedStatementSetter(){

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        RaceCardItem race = raceCardList.get(i);
                        //System.out.println(race);
                        int idx =0;
                        ps.setString(++idx,race.getRaceDate());
                        ps.setString(++idx,race.getRacePlace());

                        ps.setInt(++idx,race.getRaceSeqOfDay());
                        ps.setInt(++idx,race.getRaceClass());
                        ps.setInt(++idx,race.getDistance());
                        ps.setString(++idx,race.getGoing());

                        ps.setString(++idx,race.getCourse());
                        ps.setInt(++idx,race.getDraw());
                        ps.setString(++idx,race.getHorseId());
                        ps.setString(++idx,race.getHorseName());
                        ps.setString(++idx,race.getHorseNo());
                        ps.setInt(++idx,race.getRating());
                        ps.setString(++idx,race.getJockey());
                        ps.setString(++idx,race.getTrainer());
                        ps.setInt(++idx,race.getAddedWeight());
                        ps.setInt(++idx,race.getDeclaredHorseWeight());
                        ps.setInt(++idx,race.getRatingDelta());

                    }

                    public int getBatchSize() {
                        return raceCardList.size();
                    }
                });
    }

    public void batchUpdateRaceStatistic(final List<RaceCardItem> raceCardList,boolean isNewRace){
        String sql = isNewRace ?SQL_UPDATE_NEWRACE_STATISTIC: SQL_UPDATE_RACECARD_STATISTIC;
        this.jdbc.batchUpdate(sql,
                new BatchPreparedStatementSetter(){

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        RaceCardItem race = raceCardList.get(i);
                        //System.out.println(race);
                        int idx =0;
                        ps.setDouble(++idx,race.getHorse_winPer());
                        ps.setInt(++idx,race.getHorse_winCount());

                        ps.setInt(++idx,race.getHorse_newDistance());
                        ps.setInt(++idx,race.getHorse_newHorse());
                        ps.setDouble(++idx,race.getJockey_winPer());
                        ps.setInt(++idx,race.getJockey_winCount());
                        ps.setInt(++idx,race.getHorse_last4SpeedRate());
                        ps.setInt(++idx,race.getHorse_latestSpeedRate());
                        ps.setInt(++idx,race.getDays_from_lastRace());
                        ps.setString(++idx,race.getRaceDate());
                        ps.setInt(++idx,race.getRaceSeqOfDay());
                        ps.setString(++idx,race.getHorseId());


                    }

                    public int getBatchSize() {
                        return raceCardList.size();
                    }
                });
    }

    public List<org.andrea.jockey.model.RaceCardAnalysis> query(String SQL){

        return jdbc.query(SQL, new RaceCardAnalysisRowMapper());
    }
    public List<RaceCardResult> queryRaceResult(String SQL){

        return jdbc.query(SQL, new RaceCardRowMapper());
    }
    public List<RaceCardItem> queryNewRace(String SQL){

        return jdbc.query(SQL, new NewRaceRowMapper());
    }
    public List<HorseStatistics> queryHorseStatistics(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.HorseRowMapper());
    }
    public List<JockeyStatistics> queryJockeyStatistics(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.JockeyRowMapper());
    }
    public List<RaceStatistics> queryRaceStatistics(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.RaceRowMapper());
    }
    public int getMaxDate(){
        return jdbc.queryForObject("select max(raceDate) from racecard", Integer.class);
    }
    public List<String> getRaceDates(String daysOnAfter){
        return jdbc.query("select distinct(raceDate) from racecard where raceDate >= "+ daysOnAfter, new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
    }
    public List<String> getRaceDates( String daysOnAfter,String daysOnBefore){
        return jdbc.query("select distinct(raceDate) from racecard where raceDate <=" + daysOnBefore
                +  " and raceDate >= "+ daysOnAfter, new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
    }
    public double queryForDouble(String sql){
        System.out.println(sql);
       return jdbc.queryForObject(sql,Double.class);
    }
    public int getNewRaceDate() {
        return jdbc.queryForObject("select racedate from newrace limit 1", Integer.class);
    }
    public int getNewRaceNumber() {
        return jdbc.queryForObject("select max(raceSeqOfDay) from newrace", Integer.class);
    }
    public int getRaceSeqOfDay(int raceDate) {
        return jdbc.queryForObject("select max(raceSeqOfDay) from racecard where racedate="+raceDate, Integer.class);
    }
    public void deleteNewRace(){
        jdbc.execute("delete from newrace where 1=1");
    }

}
