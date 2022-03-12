package com.picpay.desafio.android.data.model

class UsersFactory {
    fun create(user: UserMock) = when (user) {
        UserMock.User1 -> User(
            img = "https://randomuser.me/api/portraits/men/1.jpg",
            name = "Sandrine Spinka",
            id = "1",
            username = "Tod86"
        )
        UserMock.User2 -> User(
            img = "https://randomuser.me/api/portraits/men/2.jpg",
            name = "Carli Carroll",
            id = "2",
            username = "Constantin_Sawayn"
        )
        UserMock.User3 -> User(
            img = "https://randomuser.me/api/portraits/men/3.jpg",
            name = "Annabelle Reilly",
            id = "3",
            username = "Lawrence_Nader62"
        )
    }

    sealed class UserMock {
        object User1 : UserMock()
        object User2 : UserMock()
        object User3 : UserMock()
    }
}