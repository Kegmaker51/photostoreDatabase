import java.io.*;
import java.sql.*;
import java.lang.*;

public class Photo {
    public static void main(String args[]) {
        try {
            String user = "p32001h";
            String passwd = "Security1";
            Connection con;
            Statement stmt;
            String url = "jdbc:postgresql://reddwarf.cs.rit.edu/" + user;
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, passwd);
            stmt = con.createStatement();
            createTables(stmt);
            insertRandom(stmt);
            //dumpData(stmt);
            stmt.close();
            con.close();
        } catch (Exception e) {}
    }

    private static void createTables(Statement stmt) {
        try {
            String alter = "1";
            stmt.execute("create table customer" + alter + " (" +
                    "custid serial PRIMARY KEY NOT NULL ," +
                    "firstname varchar(255)," +
                    "lastname varchar(255)," +
                    "phone NUMERIC(11,0)," +
                    "street varchar(255)," +
                    "city varchar(255)," +
                    "zipCode integer" +
                    ")");
            stmt.execute("CREATE TABLE staff" + alter + " (" +
                    "staffid serial PRIMARY KEY NOT NULL," +
                    "firstname varchar(255)," +
                    "lastname varchar(255)," +
                    "freelance bool" +
                    ")");
            stmt.execute("CREATE TABLE request" + alter + " (" +
                    "reqid serial PRIMARY KEY NOT NULL," +
                    "eventtype varchar(255)," +
                    "estimate NUMERIC(8,2)," +
                    "date timestamp, " +
                    "location varchar(255)," +
                    "staffid serial REFERENCES staff" + alter + "(staffid) on DELETE CASCADE," +
                    "custid serial REFERENCES customer" + alter + "(custid)" +
                    "on DELETE CASCADE)");
            stmt.execute("CREATE TABLE contract" + alter + " (" +
                    "contractid serial PRIMARY KEY NOT NULL," +
                    "sent TIMESTAMP," +
                    "approved TIMESTAMP," +
                    "deposit NUMERIC(8,2)," +
                    "reqid serial REFERENCES request" + alter + "(reqid)" +
                    "on DELETE CASCADE)");
            stmt.execute("CREATE TABLE prooforder" + alter + " (" +
                    "prooforderid serial PRIMARY KEY NOT NULL," +
                    "ordertype varchar(255)," +
                    "eventdate timestamp," +
                    "assistant serial REFERENCES staff" + alter + "(staffid)," +
                    "reqid serial REFERENCES request" + alter + "(reqid)" +
                    "on DELETE CASCADE)");
            stmt.execute("CREATE TABLE proof" + alter + " (" +
                    "proofid serial PRIMARY KEY NOT NULL," +
                    "proofsentdate TIMESTAMP," +
                    "expiresafter TIMESTAMP," +
                    "proofdeposit NUMERIC(8,2)," +
                    "prooforderid serial REFERENCES prooforder" + alter + "(prooforderid)" +
                    "on DELETE CASCADE)");
            stmt.execute("CREATE TABLE invoice" + alter + " (" +
                    "invoiceid serial PRIMARY KEY NOT NULL," +
                    "estimate NUMERIC(8,2)," +
                    "finaldeposit NUMERIC(8,2)," +
                    "proofid serial REFERENCES proof" + alter + "(proofid)" +
                    "on DELETE CASCADE)");
            stmt.execute("CREATE TABLE package" + alter + " (" +
                    "packageid serial PRIMARY KEY," +
                    "fees NUMERIC(8,2)," +
                    "packagetype varchar(255)," +
                    "proofid serial REFERENCES  proof" + alter + "(proofid)" +
                    "on DELETE CASCADE )");
            stmt.execute("create function date_update() returns trigger as $date_update$" +
                    "begin" +
                    "new.expiresafter := new.proofsentdate + interval '6 months';" +
                    "return new; end;" +
                    "$date_update$ language plpgsql");
            stmt.execute("create trigger date_update before insert or update of proofsentdate on proof" +
                    "for each row execute procedure date_update()");
        } catch (Exception e) {}
    }//end createTables

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
                    " (eventtype, estimate, date, location, staffid, " +
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
                System.err.println("Couldn't insert prooforder " + s);
            }
            try {
                stmt.executeUpdate("insert into proof" + alter +
                    " (proofsentdate, proofdeposit, expiresafter, " +
                    "prooforderid) values (" +
                    "'2014-11-12 12:12:12', 54, '2015-07-12 12:12:12', " +
                    s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert proof " + s);
            }
            try {
                stmt.executeUpdate("insert into invoice" + alter +
                    " (estimate, finaldeposit, proofid) values (" +
                    "54, 55, " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert invoice " + s);
            }
            try {
                stmt.executeUpdate("insert into package" + alter +
                    " (fees, packagetype, proofid) values (" +
                    "54, 'test', " + s + ")");
            } catch (Exception e) {
                System.err.println("Couldn't insert package " + s);
            }
        }
    }//end insertRandom

    /**
     * dumpData A bunch of SQL Queries to show off the Database. The result of each
     * query is printing to standard out.
     *
     * @param stmt
     * @throws SQLException
     */
    private static void dumpData(Statement stmt) throws SQLException {
        String tableListQuery = "select column_name," +
                " data_type from information_schema.columns " +
                "where table_name";
        String tableList[] = {"contract", "customer", "invoice", "package",
                "proof", "prooforder", "request", "staff"};
        try {
            System.out.println("Displaying Table Information!");
            for (String n : tableList) {
                System.out.println("Table: " + n);
                ResultSet result = stmt.executeQuery(tableListQuery + n + ";");
                while (result.next()) {
                    System.out.println(result.getString("schema") + "\t" +
                            result.getString("Name") + "\t" + result.getString("Type") +
                            "\t" + result.getString("Owner"));
                }
            }
        } catch (SQLException sql) {
            System.out.println(sql);
        }
        //Round 2!
        String joinQuery = "select customer.firstname, customer.lastname," +
                "sum(request.estimate) as estimatebycustomer " +
                "from customer join request " +
                "on customer.custid = request.custid " +
                "group by customer.firstname, customer.lastname" +
                "having sum(request.estimate) > 20";
        System.out.println("Join Query!");
        try {
            ResultSet result = stmt.executeQuery(joinQuery);
            while (result.next())
            {
                System.out.println(result.getString("firstname") + "\t" +
                        result.getString("lastname") + "\t" +
                        result.getString("estimatebycustomer"));
            }
        } catch (SQLException sql) {
            System.out.println(sql);
        }
        //Round 3
        String setQuery = "select * from contract" +
                " UNION" +
                " select * from proof" +
                " order by 1;";
        System.out.println("Union Query!");
        try {
            ResultSet result = stmt.executeQuery(setQuery);
            while(result.next())
            {
                System.out.println(result.getString("contractid") + "\t" +
                result.getString("sent") + "\t" + result.getString("approved") +
                "\t" + result.getString("deposit") + "\t" +
                result.getString("reqid"));
            }
        }catch (SQLException sql)
        {
            System.out.println(sql);
        }
        //Creating a view and querying it
        System.out.println("View StaffAvailability Created!");
        String staffView = "create view staffavailability as " +
                "select request.staffid, request.date, prooforder.eventdate " +
                "from request join proofderorder " +
                "on request.reqid = prooforder.reqid " +
                "where request.date is not null and prooforder.eventdate is not null;";
        String testView = "select * from staffavailability;";
        try{
            stmt.executeUpdate(staffView);
            ResultSet result = stmt.executeQuery(testView);
            while(result.next())
            {
                System.out.println(result.getString("staffid") + "\t" +
                result.getString("date") + "\t" + result.getString("eventdate"));
            }
        }catch (SQLException sql)
        {
            System.out.println(sql);
        }
        //Round 4 of Queries
        String existQuery = "select custid " +
                "from customer " +
                "where exists (select * from customer " +
                "where firstname like 'a%');";
        try{
            ResultSet result = stmt.executeQuery(existQuery);
            while(result.next())
            {
                System.out.println(result.getString("custid"));
            }
        }catch (SQLException sql)
        {
            System.out.println(sql);
        }
        //Round 5
        String delete = "delete from invoice where invoiceid = 1";
        System.out.println("Deleting custID 1's Data!");
        try{
            stmt.executeUpdate(delete);
            ResultSet result = stmt.executeQuery("select * from invoice");
            while(result.next())
            {
                System.out.println(result.getString("invoice") + "\t" +
                result.getString("estimate") + "\t" + result.getString("finaldeposirt") + "\t" +
                        result.getString("proofid") + "\t" + result.getString("packageid"));
            }
        }catch (SQLException sql)
        {
            System.out.println(sql);
        }
        //Round 6 Ding Ding!
        String update = "update invoice set finaldeposit = 501 where invoiceid = 2";
        System.out.println("Updating custID 2's finaldeposit!");
        try{
            stmt.executeUpdate(update);
            ResultSet result = stmt.executeQuery("select * from invoice");
            while(result.next())
            {
                System.out.println(result.getString("invoice") + "\t" +
                        result.getString("estimate") + "\t" + result.getString("finaldeposirt") + "\t" +
                        result.getString("proofid") + "\t" + result.getString("packageid"));
            }
        }catch (SQLException sql)
        {
            System.out.println(sql);
        }
    }//end dumpData
}

