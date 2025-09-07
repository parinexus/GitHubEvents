package parinexus.sample.githubevents.data.local.datasource

import androidx.room.withTransaction
import parinexus.sample.githubevents.data.local.AppDatabase
import parinexus.sample.githubevents.data.repository.port.TransactionRunner
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val db: AppDatabase
) : TransactionRunner {
    override suspend fun invoke(block: suspend () -> Unit) = db.withTransaction { block() }
}
