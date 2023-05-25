package es.joseluisgs.dam.empresa;

public class Fisgon implements ChangeEventListener {
    //Lo que va a suceder cada vez que se produzca un evento determinado
    @Override
    public void onIDChange(ChangeEvent ev) {
        //Mensaje que se imprime en consola al cambiar el ID
        System.out.println("Se cambió el ID");
    }

    @Override
    public void onNameChange(ChangeEvent ev) {
        //Mensaje que se imprime en consola al cambiar el Nombre
        System.out.println("Se cambió el nombre");
    }

    @Override
    public void onLastNameChange(ChangeEvent ev) {
        //Mensaje que se imprime en consola al cambiar el Apellido
        System.out.println("Se cambió el apellido");
    }

    @Override
    public void onAgeChange(ChangeEvent ev) {
        //Mensaje que se imprime en consola al cambiar la edad
        System.out.println("Se cambió la edad");
    }

    @Override
    public void onBusinessChange(ChangeEvent ev) {
        //Mensaje que se imprime en consola al cambiar la empresa
        System.out.println("Se cambió el nombre de la empresa");
    }
}
