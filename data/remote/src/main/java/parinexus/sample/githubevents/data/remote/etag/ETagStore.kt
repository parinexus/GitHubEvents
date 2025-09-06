package parinexus.sample.githubevents.data.remote.etag

interface ETagStore {
    fun get(key: String): String?
    fun put(key: String, etag: String)
}

class MemoryETagStore : ETagStore {
    private val map = java.util.concurrent.ConcurrentHashMap<String, String>()
    override fun get(key: String) = map[key]
    override fun put(key: String, etag: String) { map[key] = etag }
}
