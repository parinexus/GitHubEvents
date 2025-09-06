package parinexus.sample.githubevents.data.repository.store

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import parinexus.sample.githubevents.domain.model.Event

interface EventStore {
    fun put(event: Event)
    fun putAll(events: List<Event>) { events.forEach(::put) }
    fun get(id: String): Event?
}

@Singleton
class InMemoryEventStore @Inject constructor() : EventStore {
    private val map = ConcurrentHashMap<String, Event>()
    override fun put(event: Event) { map[event.id] = event }
    override fun get(id: String): Event? = map[id]
}
