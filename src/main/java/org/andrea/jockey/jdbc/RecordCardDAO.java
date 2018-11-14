package org.andrea.jockey.jdbc;



import org.andrea.jockey.model.RaceCardItem;
import org.andrea.jockey.model.RaceCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;



import javax.sql.DataSource;
import java.sql.PreparedStatement;
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
            "(rateDate," +
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
            "hourseNo," +
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
            "hourseNo," +
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

    public void insertUrls(final String url){

        this.jdbc.update("insert into test(test) values('"+url+"')");

    }

    public void batchInsertResults(final List<RaceCardResult> raceResultList){

        this.jdbc.batchUpdate(SQL_INSERT,
                new BatchPreparedStatementSetter(){

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        RaceCardResult race = raceResultList.get(i);
                        //System.out.println(race);
                        ps.setString(1,race.getRateDate());
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

    public void batchInsertNewRace(final List<RaceCardItem> raceCardList){

        this.jdbc.batchUpdate(SQL_INSERTNEWRECORD,
                new BatchPreparedStatementSetter(){

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        RaceCardItem race = raceCardList.get(i);
                        //System.out.println(race);
                        int idx =0;
                        ps.setString(++idx,race.getRateDate());
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

}
