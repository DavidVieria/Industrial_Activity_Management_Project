package project.controller.BD;

import java.sql.*;

public class BDController {

    private static final String URL = "jdbc:oracle:thin:@vsgate-s1.dei.isep.ipp.pt:10762:XE";
    private static final String USER = "David";
    private static final String PASSWORD = "Qwerty123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
