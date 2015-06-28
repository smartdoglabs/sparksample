/**
 * Created by joserubio on 6/27/15.
 */
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import static spark.Spark.*;

public class Spark {
    public static void main(String[] args) {
        get("/hello", (req, res) -> {

            Connection connection = null;
            try {
                connection = getConnection();

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
                stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
                ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

                String out = "Hello!\n";
                while (rs.next()) {
                    out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
                }

                return out;
            } catch (Exception e) {
                return "There was an error: " + e.getMessage();
            } finally {
                if (connection != null) try{connection.close();} catch(SQLException e){}
            }

        });

        get("/", (req, res) -> "Mi casa");
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        int port = dbUri.getPort();

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

}