module dev.joseluisgs.pokedexfx {
    // Librerías que vamos a usar
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires com.google.gson;
    requires kotlin.result.jvm;
    requires java.desktop;
    requires open;


    // Abrimos los dto a Gson, si no te dirá que no los conoce
    opens dev.joseluisgs.pokedexfx.dto.json to com.google.gson;

    // Abrimos y exponemos lo que va a usar desde clases con FXML
    opens dev.joseluisgs.pokedexfx to javafx.fxml;
    exports dev.joseluisgs.pokedexfx;

    // Controladores
    opens dev.joseluisgs.pokedexfx.controllers to javafx.fxml;
    exports dev.joseluisgs.pokedexfx.controllers;

    // Rutas
    opens dev.joseluisgs.pokedexfx.routes to javafx.fxml;
    exports dev.joseluisgs.pokedexfx.routes;

    // modelos
    opens dev.joseluisgs.pokedexfx.models to javafx.fxml;
    exports dev.joseluisgs.pokedexfx.models;

}