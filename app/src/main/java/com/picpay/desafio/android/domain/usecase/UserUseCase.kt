package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.data.utils.RemoteException
import kotlin.jvm.Throws

interface UserUseCase {
    suspend operator fun invoke(): List<User>
    suspend operator fun invoke(user: List<User>)
}

class UserUseCaseImpl(private val repository: UserRepository) : UserUseCase {

    override suspend fun invoke(): List<User> = repository.getUsers()

    override suspend fun invoke(user: List<User>) = repository.insertUsers(user)
}