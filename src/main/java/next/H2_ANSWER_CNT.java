package next;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by infinitu on 14. 12. 23..
 */
public class H2_ANSWER_CNT implements Trigger {

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        //Do Nothing
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        int plus = 1;
        long qid = -1;
        if (newRow != null) {
            qid = (long) newRow[4];
        }
        if (oldRow != null) {
            plus = -1;
            qid = (long) oldRow[4];
        }
        PreparedStatement prep = connection.prepareStatement(
                "UPDATE QUESTIONS SET countOfComment = countOfComment + ?" +
                        " WHERE questionId = ?;");
        prep.setInt(1, plus);
        prep.setLong(2, qid);
        prep.execute();
    }

    @Override
    public void close() throws SQLException {
        //Do Nothing
    }

    @Override
    public void remove() throws SQLException {
        //Do Nothing
    }
}