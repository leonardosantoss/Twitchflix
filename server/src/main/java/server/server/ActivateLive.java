package server.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("activate_live")
public class ActivateLive {
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(@FormParam("name") String name, @FormParam("link") String link) {
    	System.out.println("NAME: " + name);
    	System.out.println("LINK: " + link);
    	Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName( Globals.JDBC_DRIVER );

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection( Globals.DB_URL, Globals.USER, Globals.PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Retrieving from database...");
            stmt = conn.createStatement();
            
            //CHANGE QUERY
            String sql = "insert into lives (name, link)" + "values ('" + name + "', '" + link + "')";

            stmt.executeUpdate(sql);
            
            return "success";
           
        }
        catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println(se.getMessage());

        } catch (Exception e) {
            //Handle errors for Class.forName
            //e.printStackTrace();
        	System.out.println("Execption error");
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    	return "fail";
    }
}