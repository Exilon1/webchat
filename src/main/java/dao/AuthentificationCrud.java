package dao;

import java.sql.*;

/**
 * Created by Nikotin on 02.02.2017.
 */
public class AuthentificationCrud {

    private AuthentificationCrud() {
    }

    private static class SingletonHelper {
        private static final AuthentificationCrud SINGLETON = new AuthentificationCrud();
    }

    public static AuthentificationCrud getInstance() {
        return AuthentificationCrud.SingletonHelper.SINGLETON;
    }


    private Connection connection;

    public static final String CREATE_ACC = "CREATE TABLE ACC (Nickname VARCHAR(127), Password VARCHAR(255))";
    public static final String READ_ACC = "SELECT * FROM ACC";
    public static final String INSERT_INTO_ACC = "INSERT INTO ACC(Nickname, Password) VALUES(?,?)";
    public static final String CREATE_SESS = "CREATE TABLE SESS (Session VARCHAR(255), Nickname VARCHAR(127))";
    public static final String READ_SESS = "SELECT * FROM SESS";
    public static final String INSERT_INTO_SESS = "INSERT INTO SESS(Session, Nickname) VALUES(?,?)";


    private PreparedStatement createAccStatement;
    private PreparedStatement readAccStatement;
    private PreparedStatement insertAccStatement;
    private PreparedStatement createSessStatement;
    private PreparedStatement readSessStatement;
    private PreparedStatement insertSessStatement;


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
                createAccStatement = connection.prepareStatement(CREATE_ACC);
                createSessStatement = connection.prepareStatement(CREATE_SESS);
                createAccStatement.executeUpdate();
                createSessStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            readAccStatement = connection.prepareStatement(READ_ACC);
            insertAccStatement = connection.prepareStatement(INSERT_INTO_ACC);
            readSessStatement = connection.prepareStatement(READ_SESS);
            insertSessStatement = connection.prepareStatement(INSERT_INTO_SESS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if("h2Connection".equals(property)) {
            insertAccount("Alex", "123");
            insertAccount("John", "456");
        }
    }

    public void insertAccount(String nickName, String password) {
        try {
            insertAccStatement.setString(1, nickName);
            insertAccStatement.setString(2, password);
            insertAccStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyAccount(String nickName, String password) {
        boolean isVerified = false;
        try (ResultSet rs = readAccStatement.executeQuery()) {
            while (rs.next()) {
                if (rs.getString("Nickname").equals(nickName) && rs.getString("Password").equals(password)) {
                    isVerified = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isVerified;
    }

    public void insertSession(String sessonid, String nickname) {
        try {
            insertSessStatement.setString(1, sessonid);
            insertSessStatement.setString(2, nickname);
            insertSessStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSessionContains(String sessonid) {
        if (sessonid==null)
            return false;
        boolean isContains = false;
        try (ResultSet rs = readSessStatement.executeQuery()) {
            while (rs.next()) {
                 if (rs.getString("Session").equals(sessonid)) {
                    isContains = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isContains;
    }


    public void close() {
        if(createAccStatement != null)
            try {
                createAccStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(readAccStatement != null)
            try {
                readAccStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(insertAccStatement != null)
            try {
                insertAccStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        if(createSessStatement != null)
            try {
                createSessStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(readSessStatement != null)
            try {
                readSessStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(insertSessStatement != null)
            try {
                insertSessStatement.close();
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
