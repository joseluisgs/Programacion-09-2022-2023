package es.joseluisgs.dam.events;

import java.util.ArrayList;
import java.util.List;

// Alguien que dice "Hello"
public class Initiater {
    // Lista de gente que esta escuchando
    private List<HelloListener> listeners = new ArrayList<HelloListener>();

    public void addListener(HelloListener toAdd) {
        listeners.add(toAdd);
    }

    // Alguien dice "Hello"
    public void sayHello() {
        System.out.println("Initiater -> Hello!");
        // Notifica a todos los que estan escuchando
        listeners.forEach(HelloListener::someoneSaidHello);
    }
}