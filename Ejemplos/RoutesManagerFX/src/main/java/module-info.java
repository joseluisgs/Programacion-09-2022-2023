module dev.joseluisgs.routesmanagerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    opens dev.joseluisgs.routesmanagerfx to javafx.fxml;
    exports dev.joseluisgs.routesmanagerfx;

    // Controladores
    opens dev.joseluisgs.routesmanagerfx.controllers to javafx.fxml;
    exports dev.joseluisgs.routesmanagerfx.controllers;

    // Rutas
    opens dev.joseluisgs.routesmanagerfx.routes to javafx.fxml;
    exports dev.joseluisgs.routesmanagerfx.routes;

}