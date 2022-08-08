package com.picpay.desafio.android.presentation.ui.activities.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.testing.rules.MainCoroutineRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.model.UserFactory
import com.picpay.desafio.android.data.utils.ResultState
import com.picpay.desafio.android.domain.usecase.UserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var userUseCase: UserUseCase

    @Mock
    private lateinit var usersLiveDataObserver: Observer<ResultState<List<User>>>

    private lateinit var mainViewModel: MainViewModel

    private val userFactory = UserFactory()

    private val fakeUsersList = listOf(
        userFactory.create(UserFactory.UserMock.User1),
        userFactory.create(UserFactory.UserMock.User2),
        userFactory.create(UserFactory.UserMock.User3)
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        mainViewModel = MainViewModel(userUseCase)
        mainViewModel.users.observeForever(usersLiveDataObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate the ResultState is equals Success in onChanged when calling fetchUsers`() =
        runBlockingTest {
            whenever(userUseCase()).thenReturn(fakeUsersList)
            mainViewModel.fetchUsers()

            verify(usersLiveDataObserver).onChanged(ResultState.Loading)
            delay(3000)
            val result = ResultState.Success(fakeUsersList)
            verify(usersLiveDataObserver).onChanged(result)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate the ResultState is equals Loading in onChanged when calling fetchUsers`() =
        runBlockingTest {
            whenever(userUseCase()).thenReturn(fakeUsersList)
            mainViewModel.fetchUsers()

            verify(usersLiveDataObserver).onChanged(ResultState.Loading)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate the ResultState is equals Empty in onChanged when calling fetchUsers`() =
        runBlockingTest {
            whenever(userUseCase()).thenReturn(emptyList())
            mainViewModel.fetchUsers()

            verify(usersLiveDataObserver).onChanged(ResultState.Loading)
            delay(3000)
            verify(usersLiveDataObserver).onChanged(ResultState.Empty)
        }
}