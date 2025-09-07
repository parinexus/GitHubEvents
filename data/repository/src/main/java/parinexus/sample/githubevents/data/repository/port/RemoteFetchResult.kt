package parinexus.sample.githubevents.data.repository.port

data class RemoteFetchResult<T>(
    val code: Int,
    val items: List<T>,
    val linkHeader: String?
)

