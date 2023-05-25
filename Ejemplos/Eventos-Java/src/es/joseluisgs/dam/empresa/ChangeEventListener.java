package es.joseluisgs.dam.empresa;

import java.util.EventListener;

/*Se crea una interface que contenga los métodos abstractos que se dispararán cuando
 suceda algún evento para ello usamos EventListener
 */
public interface ChangeEventListener extends EventListener {

    //Método que se disparará cuando se produzca un cambio en el ID
    void onIDChange(ChangeEvent ev);

    //Método que se disparará cuando se produzca un cambio en el Nombre
    void onNameChange(ChangeEvent ev);

    //Método que se disparará cuando se produzca un cambio en el Apellido
    void onLastNameChange(ChangeEvent ev);

    //Método que se disparará cuando se produzca un cambio en la edad
    void onAgeChange(ChangeEvent ev);

    //Método que se disparará cuando se produzca un cambio en la empresa
    void onBusinessChange(ChangeEvent ev);

}