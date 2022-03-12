package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.data.utils.RemoteException
import kotlin.jvm.Throws

interface UserUseCase {
    suspend operator fun invoke(): List<User>
}

class UserUseCaseImpl(private val repository: UserRepository) : UserUseCase {
    @Throws(RemoteException::class)
    override suspend fun invoke(): List<User> = repository.getUsers()
}