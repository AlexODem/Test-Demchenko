package com.example.testdemchenko.data.local

import com.example.testdemchenko.domain.model.DatabaseMessage
import io.reactivex.Single

interface LocalDataProvider {

    fun getMessages(): Single<List<DatabaseMessage>>

    fun setMessages(messages: List<DatabaseMessage>)

    fun updateMessage(message: DatabaseMessage)
}