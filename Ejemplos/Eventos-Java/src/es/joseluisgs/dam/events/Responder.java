package es.joseluisgs.dam.events;

// Alguien interesado en la el evento Hello
public class Responder implements HelloListener {
    @Override
    public void someoneSaidHello() {
        System.out.println("Responder --> Hello there!");
    }
}