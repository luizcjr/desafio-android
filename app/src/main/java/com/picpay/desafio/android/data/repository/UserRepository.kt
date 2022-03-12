package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.data.utils.RemoteException
import retrofit2.HttpException
import kotlin.jvm.Throws

interface UserRepository {
    suspend fun getUsers(): List<User>
}

class UserRepositoryImpl(private val picPayService: PicPayService) : UserRepository {
    @Throws(RemoteException::class)
    override suspend fun getUsers(): List<User> {
        try {
            return picPayService.getUsers()
        } catch (ex: HttpException) {
            throw RemoteException("Could not connect to PicPay API")
        }
    }
}