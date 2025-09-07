package parinexus.sample.githubevents.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import parinexus.sample.githubevents.data.repository.adapter.EventsPollingAdapter
import parinexus.sample.githubevents.domain.port.EventsPollingPort

@Module
@InstallIn(ViewModelComponent::class)
abstract class PollingModule {
    @Binds
    @ViewModelScoped
    abstract fun bindEventsPollingPort(impl: EventsPollingAdapter): EventsPollingPort
}