package com.picpay.desafio.android.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.picpay.desafio.android.data.local.database.UsersDatabase
import com.picpay.desafio.android.data.local.entity.UsersEntity
import com.picpay.desafio.android.data.model.UsersFactory
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@SmallTest
class UsersDaoTest {

    private lateinit var usersDao: UsersDao
    private lateinit var db: UsersDatabase

    private val userFactory = UsersFactory()

    private val fakeUsersList = listOf(
        userFactory.create(UsersFactory.UserMock.User1),
        userFactory.create(UsersFactory.UserMock.User2),
        userFactory.create(UsersFactory.UserMock.User3)
    )

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, UsersDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        usersDao = db.usersDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUsers() = runBlocking {
        val usersEntity = fakeUsersList.map { UsersEntity.fromUser(it) }
        usersDao.insertUsers(usersEntity)
        val firstUser = usersDao.readUsers().first()

        assertEquals(firstUser, usersEntity.first())
    }
}