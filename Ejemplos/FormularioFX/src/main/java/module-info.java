module dev.joseluisgs.formulariofx {

    // Requerimos los módulos de JavaFX y Kotlin
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    // Otras dependencias


    // Abrimos los paquetes para que puedan ser accedidos por JavaFX
    opens dev.joseluisgs.formulariofx to javafx.fxml;
    exports dev.joseluisgs.formulariofx;

    // Abrimos los paquetes para que puedan ser accedidos por JavaFX
    opens dev.joseluisgs.formulariofx.controllers to javafx.fxml;
    exports dev.joseluisgs.formulariofx.controllers;
}