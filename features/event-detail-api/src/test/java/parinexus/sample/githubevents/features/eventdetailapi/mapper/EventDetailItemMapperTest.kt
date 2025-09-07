package parinexus.sample.githubevents.features.eventdetailapi.mapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.features.eventdetailapi.item.UserActor
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.features.eventdetailapi.item.UserGithubRepository
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import parinexus.sample.githubevents.features.eventdetailapi.robots.EventDetailItemMapperRobot

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class EventDetailItemMapperTest {

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: TestDispatcher
    private lateinit var robot: EventDetailItemMapperRobot

    @Before
    fun setUp() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)
        Dispatchers.setMain(dispatcher)
        robot = EventDetailItemMapperRobot()
    }

    @After
    fun tearDown() = Unit

    @Test
    fun test_Actor_toDetailActor_maps_fields() = RUN_UNIT_TEST(robot) {
        GIVEN { aFakeActor(login = "octocat", avatar = "https://img/ava.png") }
        WHEN  { mapActor() }
        THEN  { userActorIs(UserActor(login = "octocat", avatarUrl = "https://img/ava.png")) }
    }

    @Test
    fun test_RepoModel_toDetailGithubRepository_maps_fields() = RUN_UNIT_TEST(robot) {
        GIVEN { aFakeRepo(name = "hello-world", url = "https://github.com/octo/hello-world") }
        WHEN  { mapRepo() }
        THEN  { userRepoIs(UserGithubRepository(name = "hello-world", url = "https://github.com/octo/hello-world")) }
    }

    @Test
    fun test_Event_toDetailsView_maps_all_fields_with_repo() = RUN_UNIT_TEST(robot) {
        GIVEN { aFakeEvent(
            id = "42",
            login = "octocat",
            avatar = "https://img/ava.png",
            repoName = "hello-world",
            repoUrl = "https://github.com/octo/hello-world",
            createdAt = "2024-01-02T03:04:05Z"
        ) }
        WHEN  { mapEvent() }
        THEN  { userEventIs(
            UserEvent(
                id = "42",
                userActor = UserActor(login = "octocat", avatarUrl = "https://img/ava.png"),
                repo = UserGithubRepository(name = "hello-world", url = "https://github.com/octo/hello-world"),
                createdAt = "2024-01-02T03:04:05Z"
            )
        ) }
    }

    @Test
    fun test_Event_toDetailsView_handles_null_repo() = RUN_UNIT_TEST(robot) {
        GIVEN { aFakeEvent(
            id = "100",
            login = "eve",
            avatar = "https://img/eve.png",
            repoName = null,
            repoUrl = null,
            createdAt = "2024-06-06T06:06:06Z"
        ) }
        WHEN  { mapEvent() }
        THEN  {
            userEventIs(
                UserEvent(
                    id = "100",
                    userActor = UserActor(login = "eve", avatarUrl = "https://img/eve.png"),
                    repo = null,
                    createdAt = "2024-06-06T06:06:06Z"
                )
            )
            AND { userEventRepoIsNull() }
        }
    }
}
