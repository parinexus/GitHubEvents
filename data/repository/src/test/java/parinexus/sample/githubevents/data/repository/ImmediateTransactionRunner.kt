package parinexus.sample.githubevents.data.repository

import parinexus.sample.githubevents.data.repository.port.TransactionRunner

class ImmediateTransactionRunner : TransactionRunner {
    override suspend fun invoke(block: suspend () -> Unit) = block()
}