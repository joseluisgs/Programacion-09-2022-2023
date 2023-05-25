package es.joseluisgs.dam.observer;

import java.util.ArrayList;
import java.util.List;

// Se crea una clase Observable que es la que va a ser observada
public class Observable<T> {
    // Debemos tener una lista para nuestros observadores
    private List<Observer<T>> observers = new ArrayList<Observer<T>>();

    // Se crea un método para agregar observadores
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    // Se crea un método para eliminar observadores
    void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    // Se crea un método para notificar a los observadores
    public void notifyObservers(T value) {
        System.out.println("Observable --> Notificando a los observadores");
        observers.forEach(observer -> observer.update(value));
    }
}