module dev.joseluisgs.moscafx {
    // Librerías que vamos a usar
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.desktop;
    requires open;
    requires koin.logger.slf4j;
    requires koin.core.jvm;


    // Abrimos y exponemos lo que va a usar desde clases con FXML
    opens dev.joseluisgs.moscafx to javafx.fxml;
    exports dev.joseluisgs.moscafx;

    // Controladores
    opens dev.joseluisgs.moscafx.controllers to javafx.fxml;
    exports dev.joseluisgs.moscafx.controllers;

    // Rutas
    opens dev.joseluisgs.moscafx.routes to javafx.fxml;
    exports dev.joseluisgs.moscafx.routes;
}