package pl.klolo.game.event

import java.lang.IllegalArgumentException

class EventProcessor {
    private val subscription = mutableMapOf<Event, MutableList<Pair<Int /*ID*/, (Event) -> Unit>>>()

    fun subscribe(id: Int): Subscription {
        return Subscription(id, this)
    }

    class Subscription(
            private val id: Int,
            private val eventProcessor: EventProcessor) {

        fun onEvent(event: Event, eventConsumer: (Event) -> Unit): Subscription {
            eventProcessor.onEvent(event, id, eventConsumer)
            return this
        }

        fun <T: Event> onEvent(event: Class<T>, eventConsumer: (T) -> Unit): Subscription {
            val eventInstance: Event = event.newInstance() as Event
            eventProcessor.onEvent(eventInstance, id, eventConsumer as (Event) -> Unit)
            return this
        }
    }

    private fun onEvent(event: Event, id: Int, eventProcessor: (Event) -> Unit) {
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

}