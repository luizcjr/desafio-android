package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserLocalRepository

interface UsersLocalUseCase {
    suspend operator fun invoke(user: List<User>)
    operator fun invoke(): List<User>
}

class UsersLocalUseCaseImpl(
    private val userLocalRepository: UserLocalRepository
) : UsersLocalUseCase {
    override suspend fun invoke(user: List<User>) = userLocalRepository.insertUsers(user)

    override fun invoke(): List<User> = userLocalRepository.getUsers()
}
