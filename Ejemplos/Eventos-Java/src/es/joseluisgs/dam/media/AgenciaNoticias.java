package es.joseluisgs.dam.media;

import java.util.ArrayList;
import java.util.List;

public class AgenciaNoticias {
    // Lista de observadores, son los que implementan la interfaz
    private final List<Publisher<String>> listeners = new ArrayList<>();
    private final String noticia = "";


    public void setNoticias(String nuevaNoticia) {
        if (!this.noticia.equals(nuevaNoticia)) {
            System.out.println("Hay novedades en las noticias");
            listeners.forEach(l -> l.conNovedades(nuevaNoticia));
        }
    }

    // Añadimos un observador
    public void subscribe(Publisher publisher) {
        System.out.println("Nuevo suscritor");
        listeners.add(publisher);
    }

    // Eliminamos un observador
    public void unsubscribe(Publisher publisher) {
        System.out.println("Se se da de baja un suscritor");
        listeners.remove(publisher);
    }
}
