package pl.klolo.game.event

import java.lang.IllegalArgumentException
import kotlin.reflect.full.createInstance

private typealias UniqueId = String


class EventProcessor {
    private val subscription = mutableMapOf<Event, MutableList<Pair<UniqueId, (Event) -> Unit>>>()

    fun subscribe(uniqueId: String): Subscription {
        return Subscription(uniqueId, this)
    }

    class Subscription(
            private val uniqueId: String,
            private val eventProcessor: EventProcessor) {

        fun onEvent(event: Event, eventConsumer: (Event) -> Unit): Subscription {
            eventProcessor.onEvent(event, uniqueId, eventConsumer)
            return this
        }

        fun <T: Event> onEvent(event: Class<T>, eventConsumer: (T) -> Unit): Subscription {
            val eventInstance: Event = event.newInstance() as Event
            eventProcessor.onEvent(eventInstance, uniqueId, eventConsumer as (Event) -> Unit)
            return this
        }
    }

    private fun onEvent(event: Event, uniqueId: UniqueId, eventProcessor: (Event) -> Unit) {
        subscription.computeIfAbsent(event) {
            mutableListOf()
        }

        val eventProcessors = subscription[event]
        if (eventProcessors?.findLast { it.first == uniqueId } !== null) {
            throw IllegalArgumentException("[duplicate id] Event consumer already exists")
        }

        eventProcessors?.add(uniqueId to eventProcessor)
    }

    fun sendEvent(event: Event) {
        subscription
                .filter { it.key.javaClass == event.javaClass }
                .flatMap { it.value }
                .forEach { it.second(event) }
    }

}