module com.example.holajavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.holajavafx to javafx.fxml;
    exports com.example.holajavafx;
}