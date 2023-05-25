package es.joseluisgs.dam.media;

// Radio Una clase que implementa la interfaz
public class Radio implements Publisher<String> {
    @Override
    public void conNovedades(String noticias) {
        System.out.println("La radio informa: " + noticias + " en las ondas");
    }
}