package parinexus.sample.githubevents.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import parinexus.sample.githubevents.data.repository.store.EventStore
import parinexus.sample.githubevents.data.repository.store.InMemoryEventStore

@Module
@InstallIn(SingletonComponent::class)
abstract class EventStoreModule {

    @Binds
    @Singleton
    abstract fun bindEventStore(impl: InMemoryEventStore): EventStore
}
