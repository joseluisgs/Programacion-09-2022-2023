module dev.joseluisgs.holajavafxdam {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    // Logger para Kotlin
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;


    opens dev.joseluisgs.holajavafxdam to javafx.fxml;
    exports dev.joseluisgs.holajavafxdam;

    opens dev.joseluisgs.holajavafxdam.controllers to javafx.fxml;
    exports dev.joseluisgs.holajavafxdam.controllers;
}