module com.example.holajavafx {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    
                            
    opens com.example.holajavafx to javafx.fxml;
    exports com.example.holajavafx;
}