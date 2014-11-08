/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Support_Classes;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Sony
 */
public class DatabaseSupport{

ComboPooledDataSource pool;
Connection conn;
ResultSet rs = null;
Statement st = null;

public DatabaseSupport(ComboPooledDataSource p_pool)
{
    pool = p_pool;
}

public ResultSet query (String _query)
{
    try {
        conn = pool.getConnection();
        st = conn.createStatement();
        rs = st.executeQuery(_query);
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {

    }
    return rs;
}

public void close ()
{
    try {
        st.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}