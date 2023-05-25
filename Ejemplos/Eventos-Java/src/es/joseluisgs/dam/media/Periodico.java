package es.joseluisgs.dam.media;

// Radio Una clase que implementa la interfaz
public class Periodico implements Publisher<String> {
    @Override
    public void conNovedades(String noticias) {
        System.out.println("El periodico informa: " + noticias + " en papel");
    }
}