package org.seckill.test;

import java.sql.Connection;//java包  
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection  
{  
    private static String dbDriver="com.mysql.jdbc.Driver";   
    private static String dbUrl="jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf8";//根据实际情况变化  
    private static String dbUser="root";  
    private static String dbPass="root";  
    public static Connection getConn()  
    {  
        Connection conn=null;  
        try  
        {  
            Class.forName(dbDriver);  
        }  
        catch (ClassNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        try  
        {  
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//注意是三个参数  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
}  