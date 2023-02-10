package dawidborkowski.dawidborkowski;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    protected Connection connection;

    public HelloController() throws SQLException {
        setConnection();
        createTables();
    }

    protected void createTables() throws SQLException {
        String users = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR(50) NOT NULL," +
                "password VARCHAR(50) NOT NULL" +
                ");";

        String books = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR(50) NOT NULL," +
                "author VARCHAR(50) NOT NULL" +
                ");";

        String rating = "CREATE TABLE IF NOT EXISTS rating (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INT NOT NULL," +
                "book_id INT NOT NULL," +
                "rating INT NOT NULL" +
                ");";

        Statement statement = connection.createStatement();

        statement.execute(users);
        statement.execute(books);
        statement.execute(rating);

        statement.close();
    }

    protected void setConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    protected int login(String username, String password) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();

        int login = 0;

        if(result.next())
        {
            login = result.getInt("id");
        }

        statement.close();

        return login;
    }
    @FXML
    public void handleLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String username = this.username.getText();
        String password = this.password.getText();

        int login = login(username, password);

        if(login > 0)
        {
            UserSession.getInstance().setUserId(login);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
            Stage stage = (Stage) this.username.getScene().getWindow();

            stage.setTitle("Books");
            stage.setMinWidth(800);
            stage.setResizable(false);

            stage.setScene(new Scene(fxmlLoader.load()));

            stage.show();
        }
    }
}