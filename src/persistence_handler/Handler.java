/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistence_handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Handler {
         public static void setData(Connection con, String query) throws SQLException{
            Statement stm=con.createStatement();
            stm.executeUpdate(query);
        }

      
        public static ResultSet getData(Connection con, String query) throws SQLException{
            Statement stm=con.createStatement();
            ResultSet rst=stm.executeQuery(query);
            return rst;
        }
}
