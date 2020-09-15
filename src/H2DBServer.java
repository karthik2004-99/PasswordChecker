import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class H2DBServer {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/passDB";
    //for testing purposes Userid and password for database are set default
    static final String USER = "sa";
    static final String PASS = "";

    static final String ERRORTABLE ="Error Creating Database Tables";
    static final String ERRORLOCK ="Error!, Cannot Lock Account!";
    static final String PASSDOESNT= "Passwords shouldnt be equal to last 5 passwords!\nEnter new password!";
    static final String CONNDB = "Connecting to database...";
    static final String CONNDBSUCCESS = "Connected to database successfully...";
    static final String INSERTSUCCESS = "Entered the details successfully!";
    static final String INSERTFAIL = "Couldnt insert into database!";


    public void createTable(){
       try{
           Class.forName(JDBC_DRIVER);
           Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
           Statement stmt = conn.createStatement();

           String sql =  "CREATE TABLE IF NOT EXISTS DETAILS " +
                   "(id bigint AUTO_INCREMENT, " +
                   " username VARCHAR(255), " +
                   " password VARCHAR(255), " +
                   " password1 VARCHAR(255), " +
                   " password2 VARCHAR(255), " +
                   " password3 VARCHAR(255), " +
                   " password4 VARCHAR(255), " +
                   " password5 VARCHAR(255), " +
                   " time BIGINT, " +
                   "locked BOOLEAN, "+
                   " PRIMARY KEY ( id ))";

           stmt.executeUpdate(sql);
           stmt.executeUpdate(sql);
           stmt.close();
           conn.close();
       }catch (Exception e) {
           System.out.println(ERRORTABLE);
           e.printStackTrace();
       }
    }

    public boolean isUserExist(String username){
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String sql =  "SELECT * FROM DETAILS WHERE USERNAME = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next())
                return false;
            rs.close();
            stmt.close();
            conn.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean isPasswordCorrect(String username, String password){
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String sql =  "SELECT * FROM DETAILS WHERE USERNAME = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                String storedpassword = rs.getString("password");
                rs.close();
                stmt.close();
                conn.close();
                return storedpassword.equals(password);
            }
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public void lockAccount(String username){
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String sql =  "SELECT * FROM DETAILS WHERE USERNAME = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                sql = "UPDATE DETAILS " +
                        "SET LOCKED='"+true+"' WHERE USERNAME = '"+username+"';";
                stmt.executeUpdate(sql);
            }else{
                System.out.println(ERRORLOCK);
            }

        }catch (Exception e ){
            e.printStackTrace();
        }

    }
    public boolean getLockStatus(String username){
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String sql =  "SELECT * FROM DETAILS WHERE USERNAME = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                boolean lockstatus = rs.getBoolean("locked");
                rs.close();
                stmt.close();
                conn.close();
                return lockstatus;
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
        return false;
    }
    public boolean getResetStatus(String username){
        long currenttime = System.currentTimeMillis();
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String sql =  "SELECT * FROM DETAILS WHERE USERNAME = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()){
                long loggedtime = rs.getLong("time");
                long days = (currenttime-loggedtime)/86400000;
                if(days>=14)
                    return true;
                return false;
            }

        }catch (Exception e ){
            e.getLocalizedMessage();
        }
        return false;
    }
    public boolean updatePassword(String username, String newpassword){
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String sql =  "SELECT * FROM DETAILS WHERE USERNAME = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            long currentTime = System.currentTimeMillis();
            if(rs.next()){
                String pass0 = rs.getString("password");
                String pass1 = rs.getString("password1");
                String pass2 = rs.getString("password2");
                String pass3 = rs.getString("password3");
                String pass4 = rs.getString("password4");
                String pass5 = rs.getString("password5");
                if(!newpassword.equals(pass0)&&!newpassword.equals(pass1)&&!newpassword.equals(pass2)&&!newpassword.equals(pass3)&&!newpassword.equals(pass4)&&!newpassword.equals(pass5)){
                    sql = "UPDATE DETAILS " +
                            "SET password= '"+newpassword+"',password1='"+pass0+"',password2='"+pass1+"',password3='"+pass2+"',password4='"+pass3+"',password5='"+pass4+"' WHERE username = '"+username+"'";
                    stmt.executeUpdate(sql);
                    return true;
                }else{
                    System.out.println(PASSDOESNT);
                    return false;
                }
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
        return false;
    }


    public void insertData(String username, String password,long time,boolean locked){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println(CONNDB);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(CONNDBSUCCESS);
            stmt = conn.createStatement();
            String sql ="INSERT INTO DETAILS (username, password, password1, password2, password3, password4, password5, time, locked)\n" +
                    "VALUES ('"+username+"','"+password+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+time+"','"+locked+"');";
            stmt.executeUpdate(sql);
            System.out.println(INSERTSUCCESS);
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(INSERTFAIL);
            e.printStackTrace();
        }

    }

}
