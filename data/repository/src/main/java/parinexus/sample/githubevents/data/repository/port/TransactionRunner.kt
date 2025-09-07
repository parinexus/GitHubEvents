package parinexus.sample.githubevents.data.repository.port

fun interface TransactionRunner {
    suspend operator fun invoke(block: suspend () -> Unit)
}