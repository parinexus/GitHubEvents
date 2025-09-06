package parinexus.sample.githubevents.data.remote.api

import parinexus.sample.githubevents.data.remote.entity.GitHubEvent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Apis {

    @GET("events")
    suspend fun getEvents(
        @Query("per_page") limit: Int,
        @Query("page") page: Int
    ): Response<List<GitHubEvent>>
}
