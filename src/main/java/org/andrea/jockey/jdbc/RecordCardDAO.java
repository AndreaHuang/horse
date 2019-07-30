package org.andrea.jockey.jdbc;



import com.sun.org.apache.xpath.internal.operations.Div;
import org.andrea.jockey.model.*;
import org.andrea.jockey.statistics.StatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;



import javax.sql.DataSource;
import java.math.BigDecimal;
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
            " horse_last4SpeedRate =?, horse_latestSpeedRate =? , days_from_lastRace=?, "+
            " jockeyTtlCnt=?, jockeyPosCnt=?,jockeyFx=?, " +
            " jockeyTtlCnt_Distance =?, jockeyPosCnt_Distance=?,jockeyFx_Distance=?,"+
            " horseTtlCnt=?,horsePosCnt=?,horseFx=?," +
            " horseTtlCnt_Distance=?,horsePosCnt_Distance=?,horseFx_Distance=?, "+
            " propByWinOdds=?"+
            " where raceDate = ?  and RaceSeqOfDay =? and horseId =?";

    private static final String SQL_UPDATE_NEWRACE_STATISTIC="update newrace set "+
            " horse_winPer = ?, horse_winCount=?,horse_newDistance=?, " +
            " horse_newHorse =?,jockey_winPer=?,jockey_winCount=?," +
            " horse_last4SpeedRate =?, horse_latestSpeedRate =? , days_from_lastRace=? "+
            " where raceDate = ?  and RaceSeqOfDay =? and horseId =?";


    private static final String SQL_INSERT_DIVIDEND="insert into dividend (raceDate, RaceSeqOfDay," +
            "pool,winning, dividend) values" +
            "(?,?,?,?,?);";

    private static final String SQL_INSERT_SURVIVAL_ANALYSIS ="insert into survival_analysis(" +
            "raceMeeting,racedate,seq,place,distance,course," +
            "horseNoA,horseNoB,horseIdA,horseIdB,drawA,drawB,jockeyA,jockeyB," +
            "jockeyACnt,jockeyBCnt,jockeyRelRiskA2B," +
            "drawACnt,drawBCnt,drawRelRiskA2B," +
            "sameHorseACnt,sameHorseBCnt,sameHorseRelRiskA2B," +
            "diffHorseACnt,diffHorseBCnt,diffHorseRelRiskA2B," +
            "finishTimeA,finishTimeB,result) values" +
            "(?,?,?,?,?," +
            "?,?,?,?,?,?,?,?," +
            "?,?,?," +
            "?,?,?," +
            "?,?,?," +
            "?,?,?," +
            "?)";
    public void batchInsertDividend(final List<Dividend> dividendList){
        this.jdbc.batchUpdate(SQL_INSERT_DIVIDEND,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Dividend dividend = dividendList.get(i);
                        int index=1;
                        System.out.println(i +":"+ dividend.toString());
                        ps.setString(index++,dividend.getRaceDate());
                        ps.setInt(index++,dividend.getRaceSeqOfDay());
                        ps.setString(index++,dividend.getPool());
                        ps.setString(index++,dividend.getWinning());
                        ps.setBigDecimal(index++, dividend.getDividend());
                    }

                    @Override
                    public int getBatchSize() {
                        return dividendList.size();
                    }
                });
    }

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
                        System.out.println(race);
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

                        if(!isNewRace){

                            ps.setInt(++idx,race.getJockeyTtlCnt());
                            ps.setInt(++idx,race.getJockeyPosCnt());
                            ps.setDouble(++idx,race.getJockeyFx());
                            ps.setInt(++idx,race.getJockeyTtlCnt_Distance());
                            ps.setInt(++idx,race.getJockeyPosCnt_Distance());
                            ps.setDouble(++idx,race.getJockeyFx_Distance());

                            ps.setInt(++idx,race.getHorseTtlCnt());
                            ps.setInt(++idx,race.getHorsePosCnt());
                            ps.setDouble(++idx,race.getHorseFx());
                            ps.setInt(++idx,race.getHorseTtlCnt_Distance());
                            ps.setInt(++idx,race.getHorsePosCnt_Distance());
                            ps.setDouble(++idx,race.getHorseFx_Distance());
                            ps.setDouble(++idx,race.getPropByWinOdds());
                        }
                        ps.setString(++idx,race.getRaceDate());
                        ps.setInt(++idx,race.getRaceSeqOfDay());
                        ps.setString(++idx,race.getHorseId());


                    }

                    public int getBatchSize() {
                        return raceCardList.size();
                    }
                });
    }

    public void batchInsertRaceCardDraw(List<RaceCardDraw> list){

        String sql  ="insert into racecarddraw (racemeeting,distance,draw,course, ttlCount,posCount,fx)" +
                " values(?,?,?,?,?,?,?)";
        this.jdbc.batchUpdate(sql,
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {

                        RaceCardDraw item = list.get(i);

                        int idx=0;
                        ps.setString(++idx,item.getRacemeeting());
                        ps.setInt(++idx,item.getDistance());
                        ps.setInt(++idx,item.getDraw());
                        ps.setString(++idx,item.getCourse());
                        ps.setInt(++idx,item.getTtlCount());
                        ps.setInt(++idx,item.getPosCount());
                        ps.setDouble(++idx, Double.isNaN(item.getFx())? 1.0:item.getFx());
                    }

                    @Override
                    public int getBatchSize() {
                        return list.size();
                    }
                });
    }

    public void batchInsertSurvialAnalysis(List<StatUtils.SurvivalAnalysisResultWrapper> survivalResultList){

        this.jdbc.batchUpdate(SQL_INSERT_SURVIVAL_ANALYSIS,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        StatUtils.SurvivalAnalysisResultWrapper survivalResult = survivalResultList.get(i);
                        int index=1;
                        StatUtils.SurvivalAnalysisResultWrapper wrapper = survivalResultList.get(i);

                        RaceInfo raceInfo = wrapper.getRaceInfo();
                        System.out.println(i +":"+ raceInfo.toString());

                        ps.setString(index++,raceInfo.getRacePlace());
                        ps.setString(index++,raceInfo.getRaceDate());
                        ps.setInt(index++,raceInfo.getRaceSeqOfDay());
                        ps.setString(index++,raceInfo.getRacePlace());
                        ps.setInt(index++,raceInfo.getDistance());
                        ps.setString(index++,raceInfo.getCourse());

                        ps.setString(index++,wrapper.getHorseNoA());
                        ps.setString(index++,wrapper.getHorseNoB());

                        ps.setString(index++,wrapper.getHorseIdA());
                        ps.setString(index++,wrapper.getHorseIdB());

                        ps.setInt(index++,wrapper.getDrawA());
                        ps.setInt(index++,wrapper.getDrawB());
                        ps.setString(index++,wrapper.getJockeyA());
                        ps.setString(index++,wrapper.getJockeyB());

                        System.out.println(i +":"+ wrapper.getHorseNoA()+" vs " +  wrapper.getHorseNoB());
                        System.out.println(i +":"+ wrapper.getHorseIdA()+" vs " +  wrapper.getHorseIdB());
                        System.out.println(i +":"+ wrapper.getDrawA()+" vs " +  wrapper.getDrawB());
                        System.out.println(i +":"+ wrapper.getJockeyA()+" vs " +  wrapper.getJockeyB());


                        StatUtils.SurvivalAnalysisResult jockeyResult = wrapper.getJockeyResult();

                        if(jockeyResult!=null) {
                            System.out.println(i +": jockeyResult : "+ jockeyResult.toString());
                            ps.setInt(index++, jockeyResult.getCountA());
                            ps.setInt(index++, jockeyResult.getCountB());
                            ps.setBigDecimal(index++, jockeyResult.getRelativeRisk_A2B());
                        } else {
                            ps.setInt(index++, 0);
                            ps.setInt(index++, 0);
                            ps.setBigDecimal(index++, BigDecimal.ZERO);
                        }

                        StatUtils.SurvivalAnalysisResult drawResult = wrapper.getDrawResult();
                        if(drawResult!=null) {
                            System.out.println(i + ": drawResult : " + drawResult.toString());
                            ps.setInt(index++, drawResult.getCountA());
                            ps.setInt(index++, drawResult.getCountB());
                            ps.setBigDecimal(index++, drawResult.getRelativeRisk_A2B());
                        }else{
                            ps.setInt(index++, 0);
                            ps.setInt(index++, 0);
                            ps.setBigDecimal(index++,  BigDecimal.ZERO);
                        }


                        StatUtils.SurvivalAnalysisResult sameHorseResult = wrapper.getSameHorseResult();
                        if(sameHorseResult!=null) {
                            System.out.println(i + ": sameHorseResult : " + sameHorseResult.toString());
                            ps.setInt(index++, sameHorseResult.getCountA());
                            ps.setInt(index++, sameHorseResult.getCountB());
                            ps.setBigDecimal(index++, sameHorseResult.getRelativeRisk_A2B());
                        } else {
                            ps.setInt(index++, 0);
                            ps.setInt(index++, 0);
                            ps.setBigDecimal(index++,  BigDecimal.ZERO);
                        }

                        StatUtils.SurvivalAnalysisResult diffHorseResult = wrapper.getDiffHorseResult();
                        if(diffHorseResult!=null) {
                            System.out.println(i + ": diffHorseResult : " + diffHorseResult.toString());
                            ps.setInt(index++, diffHorseResult.getCountA());
                            ps.setInt(index++, diffHorseResult.getCountB());
                            ps.setBigDecimal(index++, diffHorseResult.getRelativeRisk_A2B());
                        }else {
                            ps.setInt(index++, 0);
                            ps.setInt(index++, 0);
                            ps.setBigDecimal(index++,  BigDecimal.ZERO);
                        }

                        ps.setDouble(index++,wrapper.getFinishTimeA());
                        ps.setDouble(index++,wrapper.getFinishTimeB());
                        ps.setInt(index++,wrapper.getResult());

                    }

                    @Override
                    public int getBatchSize() {
                        return survivalResultList.size();
                    }
                });
    }

    public List<org.andrea.jockey.model.RaceCardAnalysis> query(String SQL){

        return jdbc.query(SQL, new RaceCardAnalysisRowMapper());
    }
    public List<RaceCardDraw> queryRaceCardDraw(String SQL){
        return jdbc.query(SQL, new RaceCardDrawRowMapper());
    }

    public List<RaceCardResult> queryRaceResult(String SQL){

        return jdbc.query(SQL, new RaceCardRowMapper());
    }
    public List<RaceCardItem> queryNewRace(String SQL){

        return jdbc.query(SQL, new NewRaceRowMapper());
    }

    public List<RaceCardResult> queryForSurvivalAnalysis(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.SurvivalAnalysisRowMapper());
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

    public List<SurvivalAnalysis.SurvivalAnalysis_Horse> querySurvivalAnalysis_Horse(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.SurvivalAnalysis_Horse_RowMapper());
    }
    public List<SurvivalAnalysis.SurvivalAnalysis_Draw> querySurvivalAnalysis_Draw(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.SurvivalAnalysis_Draw_RowMapper());
    }
    public List<SurvivalAnalysis.SurvivalAnalysis_Jockey> querySurvivalAnalysis_Jockey(String SQL){

        return jdbc.query(SQL, new StatisticsRowMapper.SurvivalAnalysis_Jockey_RowMapper());
    }
    public List<Dividend> queryDividend(String SQL){
        return jdbc.query(SQL,new DividendRowMapper());
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
                +  " and raceDate >= "+ daysOnAfter + " order by raceDate asc", new RowMapper<String>(){
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
    public int getRaceSeqOfDay(String raceDate) {
        try {
            return jdbc.queryForObject("select max(raceSeqOfDay) from racecard where racedate=" + raceDate, Integer.class);
        }catch(Exception e){
            return 0;
        }
    }

    public void deleteNewRace(){
        jdbc.execute("delete from newrace where 1=1");
    }

    public void runSQL(String sql){
        jdbc.execute(sql);
    }

}
