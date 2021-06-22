package com.example.testdemchenko.data.datasource.repository.message

import com.example.testdemchenko.domain.model.AppQuery
import com.example.testdemchenko.domain.model.DatabaseMessage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessageRepository {

    fun getMessages(sparkQuery: AppQuery): Observable<List<DatabaseMessage>>

    fun getMessagesFromDB(): Single<List<DatabaseMessage>>

    fun updateMessage(messageResponse: DatabaseMessage)

    fun addNewMessage(messageResponse: DatabaseMessage): Observable<DatabaseMessage>

    fun updateMessageList(databaseMessage: List<DatabaseMessage>) : Completable
}