import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Alexey on 17.01.2017.
 */
public class JdbcCrud {

    private JdbcCrud() {
    }

    private static class SingletonHelper {
        private static final JdbcCrud SINGLETON = new JdbcCrud();
    }

    public static JdbcCrud getInstance() {
        return JdbcCrud.SingletonHelper.SINGLETON;
    }


    private Connection connection;
    private ResourceBundle bundle = ResourceBundle.getBundle("h2Connection");

    public static final String CREATE = "CREATE TABLE MESSAGES (Nickname VARCHAR(127), Date DATETIME(23), Message VARCHAR(255))";
    public static final String READ = "SELECT * FROM MESSAGES";
    public static final String INSERT = "INSERT INTO MESSAGES(Nickname, Date, Message) VALUES(?,?,?)";


    private PreparedStatement createStatement;
    private PreparedStatement readStatement;
    private PreparedStatement insertStatement;


    public void connect(String property)  {
        if ("postgresConnection".equals(property)) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(bundle.getString("url"), bundle.getString("user"), bundle.getString("password"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if("h2Connection".equals(property)) {
            try {
                connection = DriverManager.getConnection("jdbc:h2:mem:");
                createStatement = connection.prepareStatement(CREATE);
                createStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            createStatement = connection.prepareStatement(CREATE);
            readStatement = connection.prepareStatement(READ);
            insertStatement = connection.prepareStatement(INSERT);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insert(String nickName, Date date, String message) {
        try {
            insertStatement.setString(1, nickName);
            insertStatement.setTimestamp(2, new Timestamp(date.getTime()));
            insertStatement.setString(3, message);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String read() {
        String html = "";
        try (ResultSet rs = readStatement.executeQuery()) {
            while (rs.next()) {
                html += "<li>" + rs.getTimestamp("Nickname") + " - " + rs.getString("Date") + ": " + rs.getString("Message") + "</li>";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html;
    }


    public void close() {
        if(createStatement != null)
            try {
                createStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(readStatement != null)
            try {
                readStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(insertStatement != null)
            try {
                insertStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        if(connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

}
