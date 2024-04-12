package projectjava;

import java.sql.*;
import java.util.Scanner;

public class EventManagementSystem {
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/event_management";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;


        public static void main(String[] args) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Welcome to the Event Management System!");
                Scanner scanner = new Scanner(System.in);

                boolean exit = false;
                while (!exit) {
                    System.out.println("\nMain Menu:");
                    System.out.println("1. Manage Events");
                    System.out.println("2. View Participants");
                    System.out.println("3. View Registrations for Particular Event");
                    System.out.println("4. View Venues for Particular Event");
                    System.out.println("5. Exit");
                    System.out.print("Please enter your choice: ");
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            manageEvents(connection);
                            break;
                        case 2:
                            viewParticipants(connection);
                            break;
                        case 3:
                            viewRegistrations(connection);
                            break;
                        case 4:
                            viewVenues(connection);
                            break;
                        case 5:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                }
                scanner.close();
                System.out.println("Goodbye!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void manageEvents(Connection connection) throws SQLException {
            Scanner scanner = new Scanner(System.in);
            boolean backToMainMenu = false;
            while (!backToMainMenu) {
                System.out.println("\n(Option 1 - Manage Events)");
                System.out.println("1. Add Event");
                System.out.println("2. Update Event");
                System.out.println("3. Delete Event");
                System.out.println("4. View All Events");
                System.out.println("5. Back to Main Menu");
                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        addEvent(connection);
                        break;
                    case 2:
                        updateEvent(connection);
                        break;
                    case 3:
                        deleteEvent(connection);
                        break;
                    case 4:
                        retrieveEvents(connection);
                        break;
                    case 5:
                        backToMainMenu = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        }

        private static void addEvent(Connection connection) throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter event name:");
            String eventName = scanner.nextLine();
            System.out.println("Enter event description:");
            String eventDescription = scanner.nextLine();
            System.out.println("Enter event date (YYYY-MM-DD):");
            String eventDate = scanner.nextLine();
            System.out.println("Enter venue ID:");
            int venueId = scanner.nextInt();
            
            String sql = "INSERT INTO Events (event_name, event_description, event_date, venue_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, eventName);
                statement.setString(2, eventDescription);
                statement.setString(3, eventDate);
                statement.setInt(4, venueId);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Event added successfully");
                }
            }
        }

        private static void updateEvent(Connection connection) throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter event ID to update:");
            int eventId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println("Enter new event name:");
            String eventName = scanner.nextLine();
            System.out.println("Enter new event description:");
            String eventDescription = scanner.nextLine();
            System.out.println("Enter new event date (YYYY-MM-DD):");
            String eventDate = scanner.nextLine();
            System.out.println("Enter new venue ID:");
            int venueId = scanner.nextInt();
            
            String sql = "UPDATE Events SET event_name=?, event_description=?, event_date=?, venue_id=? WHERE event_id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, eventName);
                statement.setString(2, eventDescription);
                statement.setString(3, eventDate);
                statement.setInt(4, venueId);
                statement.setInt(5, eventId);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Event updated successfully");
                } else {
                    System.out.println("Event with ID " + eventId + " not found");
                }
            }
        }

        private static void deleteEvent(Connection connection) throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter event ID to delete:");
            int eventId = scanner.nextInt();
            
            String sql = "DELETE FROM Events WHERE event_id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, eventId);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Event deleted successfully");
                } else {
                    System.out.println("Event with ID " + eventId + " not found");
                }
            }
        }

        private static void retrieveEvents(Connection connection) throws SQLException {
            String sql = "SELECT * FROM Events";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int eventId = resultSet.getInt("event_id");
                    String eventName = resultSet.getString("event_name");
                    String eventDescription = resultSet.getString("event_description");
                    String eventDate = resultSet.getString("event_date");
                    int venueId = resultSet.getInt("venue_id");
                    System.out.println("Event ID: " + eventId);
                    System.out.println("Name: " + eventName);
                    System.out.println("Description: " + eventDescription);
                    System.out.println("Date: " + eventDate);
                    System.out.println("Venue ID: " + venueId);
                    System.out.println();
                }
            }
        }

        private static void viewParticipants(Connection connection) throws SQLException {
            String sql = "SELECT * FROM Participants";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int participantId = resultSet.getInt("participant_id");
                    String participantName = resultSet.getString("participant_name");
                    String participantEmail = resultSet.getString("participant_email");
                    String participantPhone = resultSet.getString("participant_phone");
                    System.out.println("Participant ID: " + participantId);
                    System.out.println("Name: " + participantName);
                    System.out.println("Email: " + participantEmail);
                    System.out.println("Phone: " + participantPhone);
                    System.out.println();
                }
            }
        }

        private static void viewRegistrations(Connection connection) throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter event ID:");
            int eventId = scanner.nextInt();
            String sql = "SELECT * FROM Registrations WHERE event_id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, eventId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int registrationId = resultSet.getInt("registration_id");
                        int participantId = resultSet.getInt("participant_id");
                        System.out.println("Registration ID: " + registrationId);
                        System.out.println("Participant ID: " + participantId);
                        System.out.println();
                    }
                }
            }
        }

        private static void viewVenues(Connection connection) throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter event ID:");
            int eventId = scanner.nextInt();
            String sql = "SELECT venue_name, venue_address, venue_capacity FROM Venues " +
                         "JOIN Events ON Venues.venue_id = Events.venue_id WHERE Events.event_id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, eventId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String venueName = resultSet.getString("venue_name");
                        String venueAddress = resultSet.getString("venue_address");
                        int venueCapacity = resultSet.getInt("venue_capacity");
                        System.out.println("Venue Name: " + venueName);
                        System.out.println("Address: " + venueAddress);
                        System.out.println("Capacity: " + venueCapacity);
                        System.out.println();
                    } else {
                        System.out.println("No venue found for event ID " + eventId);
                    }
                }
            }
        }
    }

    
