package com.picpay.desafio.android.presentation.ui.activities.main

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.R
import com.picpay.desafio.android.RecyclerViewMatchers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityTest {

    private lateinit var server: MockWebServer
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        server = MockWebServer().apply {
            start(serverPort)
        }
    }

    @Test
    fun shouldShowProgressBar() {
        launchActivity<MainActivity>().apply {
            server.enqueue(successResponse)
            moveToState(Lifecycle.State.RESUMED)
            onView(withId(R.id.user_list_progress_bar)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayTitle() {
        launchActivity<MainActivity>().apply {
            server.enqueue(successResponse)
            val expectedTitle = context.getString(R.string.title)

            moveToState(Lifecycle.State.RESUMED)
            onView(isRoot()).perform(waitFor())
            onView(withText(expectedTitle)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayListItem() {
        launchActivity<MainActivity>().apply {
            server.enqueue(successResponse)
            moveToState(Lifecycle.State.RESUMED)
            onView(isRoot()).perform(waitFor())
            RecyclerViewMatchers.atPosition(1, isDisplayed())
        }
    }

    @Test
    fun shouldVerifyRecyclerViewItem() {
        launchActivity<MainActivity>().apply {
            server.enqueue(successResponse)
            moveToState(Lifecycle.State.RESUMED)
            onView(isRoot()).perform(waitFor())
            RecyclerViewMatchers.checkRecyclerViewItem(R.id.recyclerView, 0, isDisplayed())
        }
    }

    @Test
    fun shouldShowErrorLayout() {
        launchActivity<MainActivity>().apply {
            server.enqueue(errorResponse)
            moveToState(Lifecycle.State.RESUMED)
            onView(isRoot()).perform(waitFor())
            onView(
                withId(R.id.include_layout_error)
            ).check(
                matches(isDisplayed())
            )
        }
    }

    @Test
    fun shouldShowEmptyLayout() {
        launchActivity<MainActivity>().apply {
            server.enqueue(emptyResponse)
            moveToState(Lifecycle.State.RESUMED)
            onView(isRoot()).perform(waitFor())
            onView(withId(R.id.include_layout_empty)).check(matches(isDisplayed()))
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun waitFor(): ViewAction {
        return object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription(): String = "wait for $5000 milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(5000)
            }
        }
    }

    companion object {
        private const val serverPort = 8080

        private val successResponse by lazy {
            val body =
                "[{\"id\":1001,\"name\":\"Eduardo Santos\",\"img\":\"https://randomuser.me/api/portraits/men/9.jpg\",\"username\":\"@eduardo.santos\"}]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val emptyResponse by lazy {
            val body =
                "[]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse by lazy { MockResponse().setResponseCode(404) }
    }
}