package com.picpay.desafio.android.domain.di

import com.picpay.desafio.android.domain.usecase.UserUseCase
import com.picpay.desafio.android.domain.usecase.UserUseCaseImpl
import com.picpay.desafio.android.domain.usecase.UsersLocalUseCase
import com.picpay.desafio.android.domain.usecase.UsersLocalUseCaseImpl
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    fun load() {
        loadKoinModules(useCaseModule())
    }

    private fun useCaseModule(): Module {
        return module {
            factory<UserUseCase> { UserUseCaseImpl(get()) }
            factory<UsersLocalUseCase> { UsersLocalUseCaseImpl(get()) }
        }
    }
}