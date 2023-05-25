package es.joseluisgs.dam.empresa;

import java.util.EventObject;

//Se crea una clase que herede a EventObject
public class ChangeEvent extends EventObject {

    Empresa empresa;

    public ChangeEvent(Object source, Empresa empresa) {
        super(source);
        this.empresa = empresa;
    }
}