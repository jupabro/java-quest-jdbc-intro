package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        final String DRIVER_MYSQL = "com.mysql.cj.jdbc.Driver";
        final String DRIVER_POSTGRESQL = "org.postgresql.Driver";
        final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";

        final String URL = "jdbc:mysql://localhost:3306/jdbc_quest_db";
        final String USERNAME = "user";
        final String PASSWORD = "pwd";

        try {
            // Load the MySQL JDBC driver
            Class.forName(DRIVER_MYSQL);

            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute a query
            ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM persons");

            while (resultSet.next()) {
                int count = resultSet.getInt(1);
                System.out.println("Total persons in the table: " + count);
            }

            // Close resources
            resultSet.close();
            statement.close();
            // Close the connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Inserting a new person
        String insertQuery = "INSERT INTO persons (firstname, lastname, age) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, "Mila");
            preparedStatement.setString(2, "Cat");
            preparedStatement.setInt(3, 7);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new person was inserted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retrieving all persons
        String selectAllQuery = "SELECT * FROM persons";

        // here the try block will automatically close the Connection and PreparedStatement
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectAllQuery)) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int age = resultSet.getInt("age");

                System.out.println("Name: " + firstName + " " + lastName + ", Age: " + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Updating the lastname
        String updateQuery = "UPDATE persons SET lastname = ? WHERE firstname = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, "NewLastname");
            preparedStatement.setString(2, "Mila");

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Lastname updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Deleting records
        String deleteQuery = "DELETE FROM persons WHERE age < ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, 25);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(rowsDeleted + " record(s) deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}