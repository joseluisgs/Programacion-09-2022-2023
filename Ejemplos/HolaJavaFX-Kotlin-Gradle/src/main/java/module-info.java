module dev.joseluisgs.holajavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens dev.joseluisgs.holajavafx to javafx.fxml;
    exports dev.joseluisgs.holajavafx;
}