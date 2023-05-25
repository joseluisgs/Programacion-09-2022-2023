package es.joseluisgs.dam.empresa;

import java.util.ArrayList;

public class Empresa {
    //Lista de manejadores de eventos
    private static ArrayList listeners;

    //Variables de la clase Empresa
    private String id;
    private String nombre;
    private String apellido;
    private int edad;
    private String empresa;

    public Empresa(String id, String nombre, String apellido, int edad, String empresa) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.empresa = empresa;

        listeners = new ArrayList();
    }

    //Se agregan manejadores de eventos
    public void addEventListener(ChangeEventListener listener) {
        listeners.add(listener);
    }

    //Método de disparo del evento onNameChange
    private void triggerNameEvent() {
        System.out.println("Se ha producido un cambio en el nombre");
        changeProperties();
    }

    private void changeProperties() {
        listeners.forEach(listener -> {
                    ChangeEvent readerEvObj = new ChangeEvent(this, this);
                    ((ChangeEventListener) listener).onNameChange(readerEvObj);
                }
        );
    }

    //Método de disparo del evento onLastNameChange
    private void triggerLastNameEvent() {
        System.out.println("Se ha producido un cambio en el apellido");
        changeProperties();
    }

    //Método de disparo del evento onIDChange
    private void triggerIDEvent() {
        System.out.println("Se ha producido un cambio en el id");
        changeProperties();
    }

    //Método de disparo del evento onAgeChange
    private void triggerAgeEvent() {
        System.out.println("Se ha producido un cambio en la edad");
        changeProperties();
    }

    //Método de disparo del evento onBusinessChange
    private void triggerBusinessEvent() {
        System.out.println("Se ha producido un cambio en la empresa");
        changeProperties();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        //disparo del evento onIDChange
        this.triggerIDEvent();
    }

    public String getName() {
        return nombre;
    }

    public void setName(String nombre) {
        this.nombre = nombre;
        //disparo del evento onNameChange
        this.triggerNameEvent();
    }

    public String getLastName() {
        return apellido;
    }

    public void setLastName(String apellido) {
        this.apellido = apellido;
        //disparo del evento onLastNameChange
        this.triggerLastNameEvent();
    }

    public int getAge() {
        return edad;
    }

    public void setAge(int edad) {
        this.edad = edad;
        //disparo del evento onAgeChange
        this.triggerAgeEvent();
    }

    public String getBuiness() {
        return empresa;
    }

    public void setBusiness(String empresa) {
        this.empresa = empresa;
        //disparo del evento onBusinessChange
        this.triggerBusinessEvent();
    }

}