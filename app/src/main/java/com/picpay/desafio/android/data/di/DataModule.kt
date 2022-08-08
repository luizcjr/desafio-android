package com.picpay.desafio.android.data.di

import android.app.Application
import androidx.room.Room
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.data.local.dao.UsersDao
import com.picpay.desafio.android.data.local.database.UsersDatabase
import com.picpay.desafio.android.data.local.datasource.LocalDataSource
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.data.service.PicPayService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DataModule {

    private const val connectionTimeoutSeconds = 20 * 1000L
    private const val url = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"
    private const val database_name = "users_database"

    fun load() {
        loadKoinModules(repositoryModule() + networkModule() + localModule())
    }

    private fun localModule(): Module {
        return module {
            single { provideDataBase(androidApplication()) }
            single { provideDao(get()) }
            single { LocalDataSource(get()) }
        }
    }

    private fun provideDataBase(application: Application): UsersDatabase {
        return Room.databaseBuilder(application, UsersDatabase::class.java, database_name)
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideDao(dataBase: UsersDatabase): UsersDao {
        return dataBase.usersDao()
    }

    private fun repositoryModule(): Module {
        return module {
            single<UserRepository> { UserRepositoryImpl(get(), get(), androidContext()) }
        }
    }

    private fun networkModule(): Module {
        return module {
            single<PicPayService> { createService(get()) }

            single {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val okHttpBuilder = OkHttpClient.Builder()
                okHttpBuilder.readTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
                okHttpBuilder.writeTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
                okHttpBuilder.callTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
                okHttpBuilder.connectTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
                okHttpBuilder.addInterceptor(loggingInterceptor)
                if (BuildConfig.DEBUG) {
                    okHttpBuilder.addInterceptor(OkHttpProfilerInterceptor())
                }
                okHttpBuilder.build()
            }
        }
    }

    /**
     * Para diminuir o custo de alocações de memória causadas por expressões lambda, use a palavra-chave inline.
     * Use em linha com pequenas funções. Marque o parâmetro da função como não embutido se quiser manter ou
     * passar a referência dele.
     * A palavra-chave reified é usada para acessar as informações de tipo do objeto em funções genéricas.
     */
    private inline fun <reified T> createService(
        client: OkHttpClient
    ): T {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }
}