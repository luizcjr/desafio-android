package com.picpay.desafio.android.domain.usecase

import com.example.testing.rules.MainCoroutineRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.model.UserFactory
import com.picpay.desafio.android.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserUseCaseImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var userRepository: UserRepository

    private lateinit var userUseCase: UserUseCase

    private val userFactory = UserFactory()

    private val fakeUsersList = listOf(
        userFactory.create(UserFactory.UserMock.User1),
        userFactory.create(UserFactory.UserMock.User2),
        userFactory.create(UserFactory.UserMock.User3)
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        userUseCase = UserUseCaseImpl(userRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate data when invoke from use case is called`() = runBlockingTest {
        whenever(userRepository.getUsers()).thenReturn(fakeUsersList)

        val result = userUseCase.invoke()

        verify(userRepository).getUsers()
        Assert.assertNotNull(result.first())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate data is empty when invoke from use case is called`() = runBlockingTest {
        whenever(userRepository.getUsers()).thenReturn(emptyList())

        val result = userUseCase.invoke()

        verify(userRepository).getUsers()
        Assert.assertTrue(result.isEmpty())
    }
}