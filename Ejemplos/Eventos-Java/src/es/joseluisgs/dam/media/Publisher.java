package es.joseluisgs.dam.media;

// Una interfaz, podríamos usar genéricos
public interface Publisher<T> {
    void conNovedades(T noticias);
}
