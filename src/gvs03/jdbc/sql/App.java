package gvs03.jdbc.sql;

import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

public class App {

    static final String DB_URL = "jdbc:mysql://localhost:3306/phonebook";
    static final String USER = "root";
    static final String PASS = "FhpDxFJYTEuBC1zsi17C";

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Соединение с БД
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM my_phonebook");

            // Выводим начальные данные
            System.out.println("Initial list of records:");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " - " + rs.getString("number"));
            }
            System.out.println("================================\n");

            // Увеличиваем каждый номер на случайное число (100-1000)
            System.out.println("Increasing each number by random number (100-1000)...\n");
            rs.beforeFirst();
            while (rs.next()) {
                String oldNumber = rs.getString("number");
                String newNumber = increaseNumber(oldNumber);
                rs.updateString("number", newNumber);
                rs.updateRow();
            }

            // Выводим видоизмененные данные
            System.out.println("Final list of records:");
            rs.beforeFirst();
            while (rs.next()) {
                System.out.println(rs.getString("name") + " - " + rs.getString("number"));
            }
            System.out.println("================================\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Increase the number by random int in range 100-1000.
     *
     * @param numberStr
     */
    private static String increaseNumber(String numberStr) {
        return Long.toString(Long.parseLong(numberStr) + ThreadLocalRandom.current().nextInt(100, 1000));
    }
}
