package es.joseluisgs.dam.observer;

// Class observer, se dedica a observar
public class Observer<T> {
    private final int id;
    // Constructor
    public Observer(int id) {
        this.id = id;
    }
    // Se crea un método para actualizar cuando nos llamen
    void update(T value) {
        System.out.println("Observer "+id+": "+value);
    }
}