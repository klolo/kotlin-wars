package pl.klolo.game.event

class EventProcessor {
    private val subscription = mutableMapOf<Event, MutableList<Pair<Int /*ID*/, (Event) -> Unit>>>()

    fun subscribe(id: Int): Subscription {
        return Subscription(id, this)
    }

    class Subscription(val id: Int, val eventProcessor: EventProcessor) {

        fun onEvent(event: Event, eventConsumer: (Event) -> Unit): Subscription {
            eventProcessor.onEvent(event, id, eventConsumer)
            return this
        }

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : Event> onEvent(noinline eventConsumer: (T) -> Unit): Subscription {
            val eventInstance = if (T::class.objectInstance != null) T::class.objectInstance else T::class.java.newInstance()
            eventProcessor.onEvent(eventInstance as Event, id, eventConsumer as (Event) -> Unit)
            return this
        }
    }

    fun onEvent(event: Event, id: Int, eventProcessor: (Event) -> Unit) {
        subscription.computeIfAbsent(event) {
            mutableListOf()
        }

        val eventProcessors = subscription[event]
        if (eventProcessors?.findLast { it.first == id } !== null) {
            throw IllegalArgumentException("[duplicate id] Event consumer already exists")
        }

        eventProcessors?.add(id to eventProcessor)
    }

    fun sendEvent(event: Event) {
        subscription
                .filter { it.key.javaClass == event.javaClass }
                .flatMap { it.value }
                .forEach { it.second(event) }
    }

    fun sendEvent(event: Event, destinationId: Int) {
        subscription
                .filter { it.key.javaClass == event.javaClass }
                .flatMap { it.value }
                .filter { it.first == destinationId }
                .forEach { it.second(event) }
    }

    fun clearAllSubscription() {
        subscription.clear()
    }
}