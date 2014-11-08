package Support_Classes;

import java.sql.*;
/**
 *
 * @author Isuru
 */
public class DBConnect {
    
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    
    public DBConnect(){  
        try{       
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/recsystem","root","");
            st = con.createStatement();     
        }catch(Exception e){
            System.out.println("DB connect error"+e);    
        }  
    }//end of constructor
    
    public Connection getcn(){
        return con;
    }
    
    public Statement getst(){
        return st;
    }
    
}//end of class
