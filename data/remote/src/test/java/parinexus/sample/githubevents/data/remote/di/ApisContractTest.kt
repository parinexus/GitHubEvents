package parinexus.sample.githubevents.data.remote.di

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.remote.api.Apis
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ApisContractTest {

    @get:Rule val hilt = HiltAndroidRule(this)

    @Inject lateinit var apis: Apis
    @Inject lateinit var server: MockWebServer

    private lateinit var robot: ApisContractRobot

    @Before
    fun setup() {
        hilt.inject()
        robot = ApisContractRobot(server, apis)
    }

    @After
    fun tearDown() {
        try { server.shutdown() } catch (_: Exception) {}
    }

    @Test
    fun getEvents_builds_correct_path_and_query() = RUN_UNIT_TEST(robot) {
        GIVEN {
            server.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("[]")
                    .addHeader("Content-Type", "application/json")
            )
        }
        WHEN  { apis.getEvents(limit = 30, page = 2) }
        THEN  { robot.captureRequest(); robot.assertPathAndQuery() }
        AND   { robot.assertHeaders() }
    }
}