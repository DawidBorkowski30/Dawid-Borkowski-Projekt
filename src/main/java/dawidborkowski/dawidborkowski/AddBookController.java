package dawidborkowski.dawidborkowski;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBookController {

    @FXML
    protected TextField title;

    @FXML
    protected TextField author;
    public void addBook(MouseEvent mouseEvent) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

        String sql = "INSERT INTO books VALUES (NULL, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, title.getText());
        statement.setString(2, author.getText());

        statement.execute();

        title.getScene().getWindow().hide();
    }
}
