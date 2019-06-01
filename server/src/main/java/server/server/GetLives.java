package server.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("get_lives")
public class GetLives {
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getIt() {
    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
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

            String sql = "Select * from lives";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Retrieved from database...");
            
            while (rs.next()) {
            	arrayBuilder.add(Json.createObjectBuilder()
        			.add("idlives", rs.getInt("idlives"))
        			.add("name", rs.getString("name"))
        			.build());
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
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
        return arrayBuilder.build();
    }
}
