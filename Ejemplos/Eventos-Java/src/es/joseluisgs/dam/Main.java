package es.joseluisgs.dam;


import es.joseluisgs.dam.empresa.Empresa;
import es.joseluisgs.dam.empresa.Fisgon;
import es.joseluisgs.dam.events.Initiater;
import es.joseluisgs.dam.events.Responder;
import es.joseluisgs.dam.media.AgenciaNoticias;
import es.joseluisgs.dam.media.Periodico;
import es.joseluisgs.dam.media.Radio;
import es.joseluisgs.dam.observer.Observable;
import es.joseluisgs.dam.observer.Observer;

/**
 * La base del patron de eventos es el observador. y así usamos la reactividad
 */
public class Main {

    public static void main(String[] args) {
        testObserver();
        testNoticias();
        testEventos();
        testEmpresa();
    }

    private static void testEmpresa() {
        System.out.println();
        System.out.println("Ejemplo con Eventos y Listener de la empresa");
        //Se crea un objeto de la clase empresa
        Empresa empresa = new Empresa("1-234-567", "Pepe", "García", 22, "Leganes Hitek");
        Fisgon fisgon01 = new Fisgon();
        Fisgon fisgon02 = new Fisgon();
        //Se agrega el manejador de eventos
        empresa.addEventListener(fisgon01);
        empresa.addEventListener(fisgon02);

        //Cambio los valores iniciales por unos nuevos
        empresa.setId("7-654-321");
        empresa.setName("Luis");
        empresa.setLastName("Navarro");
        empresa.setAge(24);
    }

    private static void testNoticias() {
        System.out.println();
        System.out.println("Ejemplo Patrón Observer Medios de Noticias");
        // Preparamos los objetos
        var radioChannel = new Radio();
        var newspaper = new Periodico();
        var newsAgency = new AgenciaNoticias();

        // Suscribimos a la agencia, me observan
        newsAgency.subscribe(radioChannel);
        newsAgency.subscribe(newspaper);

        // Lanzamos una noticia. Al estar el delegado, la observa y automaticamente notifica
        // A mis observadores
        newsAgency.setNoticias("¡Nadal Gana!");
        newsAgency.setNoticias("¡Hoy llueve!");
        newsAgency.setNoticias("¡Todos han aprobado!");

        // Los periódicos se retiran
        newsAgency.unsubscribe(newspaper);
        newsAgency.setNoticias("Llegan las vacaciones :)");
    }

    private static void testObserver() {
        System.out.println();
        System.out.println("Ejemplo Patrón Observer");
        var observable = new Observable<String>();
        var observer1 = new Observer<String>(1);
        var observer2 = new Observer<String>(2);
        observable.addObserver(observer1);
        observable.addObserver(observer2);
        observable.notifyObservers("Hello");
    }

    private static void testEventos() {
        System.out.println();
        System.out.println("Ejemplo Listener");
        var initiater = new Initiater();
        var responder = new Responder();
        initiater.addListener(responder);
        initiater.sayHello();
    }
}
