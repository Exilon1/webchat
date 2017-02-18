package dao;

import java.sql.*;
import java.util.Date;

/**
 * Created by Alexey on 17.01.2017.
 */
public class MessagesCrud {

    private MessagesCrud() {
    }

    private static class SingletonHelper {
        private static final MessagesCrud SINGLETON = new MessagesCrud();
    }

    public static MessagesCrud getInstance() {
        return MessagesCrud.SingletonHelper.SINGLETON;
    }


    private Connection connection;

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
                connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/test", "postgres", "postgres");
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
        if("h2Connection".equals(property)) {
            insert("Alex", new Date(), "Some message");

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
                html += "<li>" + rs.getString("Nickname") + " - " + rs.getTimestamp("Date") + ": " + rs.getString("Message") + "</li>";
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
