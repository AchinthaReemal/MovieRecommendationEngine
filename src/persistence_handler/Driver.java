/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistence_handler;

import Support_Classes.DatabaseSupport;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Driver {
   
    public static Connection connect() throws ClassNotFoundException, SQLException {
    
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/recsystem", "root", "");
        return con;
        
    }
    
    public static DatabaseSupport  createPool(){
        try {
            System.out.println("checked----3");
            ComboPooledDataSource pool = new ComboPooledDataSource();
            System.out.println("checked----4");
            pool.setDriverClass("com.mysql.jdbc.Driver");
            pool.setJdbcUrl("jdbc:mysql://localhost:3306/recsystem");
            pool.setUser("root");
            pool.setPassword("arpc_mcg");
            pool.setMaxPoolSize(100);
            pool.setMinPoolSize(10);
            System.out.println("checked----4");
            
            DatabaseSupport database = new DatabaseSupport(pool);
            return database;
            
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    
    
}
