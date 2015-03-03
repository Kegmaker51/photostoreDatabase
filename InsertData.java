import java.io.*;
import java.sql.*;
import java.lang.*;

public class InsertData {

    public static void main (String args[]) {
        try {
            String user = "p32001h";
            String passwd = "Security1";
            Connection con;
            Statement stmt;
            String url = "jdbc:postgresql://reddwarf.cs.rit.edu/" + user;
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, passwd);
            stmt = con.createStatemnt();
            insertRandom(stmt);
            stmt.close();
            con.close();
        }
        catch (Exception e) {}
    }
    
    private static void insertRandom(Statement stmt) {
        String base = "abc";
        String alter = "1";
        for (int i = 1; i < 41; ++i) {
            String s = Integer.toString(i);
            String b = "false";
            if ((i % 4) == 0) {
                b = "true";
            }
            try {
                stmt.executeUpdate("insert into customer" + alter + " (firstname,lastname," +
                    "phone, street, city, zipcode) values ('" + base + s + "', '" +
                    base + s + "', 585475" + s + ", 'street " +  s +
                    "', 'rochester', 1234" + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert customer " + s);
            }
            try {
                stmt.executeUpdate("insert into staff" + alter + " (firstname, lastname, " +
                    "freelance) values ('" + s + base + "', '" + s + base +
                    "', " + b + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert staff " + s);
            }
            try {
                stmt.executeUpdate("insert into request" + alter +
                    " (type, estimate, date, location, staffid, " +
                    "custid) values ('Meeting', 30, " +
                    "'2014-11-12 12:00:00', 'Studio', " + s + ", " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert request " + s);
            }
            try {
                stmt.executeUpdate("insert into contract" + alter +
                    " (sent, deposit, reqid) values (" +
                    "'2014-11-12 12:12:12', 33, " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert contract " + s);
            }
            try {
                stmt.executeUpdate("insert into prooforder" + alter +
                    " (ordertype, eventdate, reqid) values (" +
                    "'" + base + s + base + "', " + 
                    "'2014-11-08 11:13:00', " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert contract " + s);
            }
            try {
                stmt.executeUpdate("insert into proof" + alter +
                    " (proofsentdate, proofdeposit, expiresafter, " +
                    "prooforderid) values (" +
                    "'2014-11-12 12:12:12', 54, '2015-07-12 12:12:12', " +
                    s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert contract " + s);
            }
            try {
                stmt.executeUpdate("insert into invoice" + alter +
                    " (estimate, finaldeposit, proofid) values (" +
                    "54, 55, " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert contract " + s);
            }
            try {
                stmt.executeUpdate("insert into package" + alter +
                    " (fees, packagetype, proofid) values (" +
                    "54, 'test', " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert contract " + s);
            }
        }
    }
}
