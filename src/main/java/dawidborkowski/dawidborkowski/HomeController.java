package dawidborkowski.dawidborkowski;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    protected Connection connection;

    @FXML
    private TableView table;

    protected ObservableList<Book> books;

    public HomeController() throws SQLException {
        setConnection();

        books = FXCollections.observableArrayList();
    }

    protected void setBooks() throws SQLException {
        String sql = "SELECT books.id, books.title, books.author, AVG(rating.rating) as rating FROM books LEFT JOIN rating ON books.id = rating.book_id GROUP BY books.id;";
        Statement statement = connection.createStatement();

        ResultSet result = statement.executeQuery(sql);

        while(result.next())
        {
            books.add(new Book(result.getInt("id"), result.getString("title"), result.getString("author"), String.valueOf(result.getInt("rating"))));
        }

        statement.close();
    }

    protected void setConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<Book, String> titleColumn = new TableColumn<>("Tytuł");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(300);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Autor");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setMinWidth(150);

        TableColumn<Book, String> ratingColumn = new TableColumn<>("Ocena");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Book, Book> cc = new TableColumn<>("Oceń");
        cc.setCellValueFactory(new PropertyValueFactory<>(""));
        cc.setCellFactory(param -> new TableCell<Book, Book>() {
            private TextField textField = new TextField();

            @Override
            protected void updateItem(Book bookRow, boolean empty) {
                super.updateItem(bookRow, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Button rateButton = new Button("Oceń");
                rateButton.setOnMouseClicked(event -> {
                    Book book = getTableView().getItems().get(getIndex());

                    rateBook(book, Integer.valueOf(textField.getText()));
                });

                HBox hBox = new HBox(textField, new Separator(), rateButton);
                setGraphic(hBox);
            }
        });

        table.getColumns().addAll(titleColumn, authorColumn, ratingColumn, cc);
        table.setItems(books);

        try {
            setBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rateBook(Book book, Integer text) {
       if(text >= 0 && text <= 5)
       {
           String sql = "INSERT INTO rating VALUES (NULL, ?, ?, ?);";

           PreparedStatement statement = null;
           try {
               statement = connection.prepareStatement(sql);

               statement.setInt(1, UserSession.getInstance().getUserId());
               statement.setInt(2, book.getId());
               statement.setInt(3, text);

               statement.execute();

               statement.close();
           }
           catch (SQLException e) {
               throw new RuntimeException(e);
           }

           books.removeAll(books);

           try {
               this.setBooks();
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
    }

    public void addBook(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                getClass().getResource("addBook.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Add Book");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)mouseEvent.getSource()).getScene().getWindow() );
        stage.showAndWait();

        books.removeAll(books);

        try {
            this.setBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
