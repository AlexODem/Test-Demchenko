package com.example.testdemchenko.data.local

import com.example.testdemchenko.domain.model.DatabaseMessage
import io.reactivex.Single

class RoomDatabaseDataProvider(
    private val appDatabase: AppDatabase
) : LocalDataProvider {

    override fun getMessages(): Single<List<DatabaseMessage>> {
        return appDatabase.messagesDao().getAll()
    }

    override fun setMessages(messages: List<DatabaseMessage>) {
        appDatabase.messagesDao().insert(messages)
    }

    override fun updateMessage(message: DatabaseMessage) {
        return appDatabase.messagesDao().update(message)
    }

}