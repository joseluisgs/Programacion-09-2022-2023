package delegate

import pattern.NewsAgency
import pattern.Newspaper
import pattern.Publisher
import pattern.RadioChannel
import kotlin.properties.Delegates

/*
https://refactoring.guru/es/design-patterns/observer
 */

// Una interfaz, podríamos usar genéricos
interface Publisher {
    fun onNews(news: String)
}

// Radio Una clase que implementa la interfaz
// reaccionamos ante un cambio
class RadioChannel : Publisher {
    override fun onNews(news: String) = println("La radio informa: $news")
}

// Periodico Una clase que implementa la interfaz
// reaccionamos ante un cambio
class Newspaper : Publisher {
    override fun onNews(news: String) = println("El periódico informa: $news")
}

// Agencia que es observada
class NewsAgency {
    // Lista de observadores, son los que implementan la interfaz
    private val listeners = mutableListOf<Publisher>()

    // Si cambia la noticia, se notifica a los observadores o reaccionamos
    var news: String by Delegates.observable("") { _, _, newValue ->
        // Cuando se actualiza la noticia, se notifica a los observadores
        listeners.forEach { it.onNews(newValue) }
    }

    // Añadimos un observador
    fun subscribe(publisher: Publisher) = listeners.add(publisher)

    // Eliminamos un observador
    fun unsubscribe(publisher: Publisher) = listeners.remove(publisher)
}

fun main() {
    // Preparamos los objetos
    val radioChannel = RadioChannel()
    val newspaper = Newspaper()
    val newsAgency = NewsAgency()

    // Suscribimos a la agencia, me observan
    newsAgency.subscribe(radioChannel)
    newsAgency.subscribe(newspaper)

    // Lanzamos una noticia. Al estar el delegado, la observa y automaticamente notifica
    // A mis observadores
    newsAgency.news = "¡Nadal Gana!"
    newsAgency.news = "¡Hoy llueve!"
    newsAgency.news = "¡Todos han aprobado!"

    // Los periódicos se retiran
    newsAgency.unsubscribe(newspaper)
    newsAgency.news = "Llegan las vacaciones :)"
}