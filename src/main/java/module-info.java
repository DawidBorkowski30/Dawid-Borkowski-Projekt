module dawidborkowski.dawidborkowski {
    requires javafx.controls;
    requires javafx.fxml;
    requires sqlite.jdbc;
    requires java.sql;


    opens dawidborkowski.dawidborkowski to javafx.fxml;
    exports dawidborkowski.dawidborkowski;
}