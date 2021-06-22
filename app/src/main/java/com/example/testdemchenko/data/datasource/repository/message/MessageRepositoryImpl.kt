package com.example.testdemchenko.data.datasource.repository.message

import com.example.sparktestdemchenko.data.datasource.provider.base.RepositoryProvider
import com.example.sparktestdemchenko.data.datasource.repository.base.BaseRepository
import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.MessageResponse
import com.example.testdemchenko.domain.model.AppQuery
import com.example.testdemchenko.domain.model.DatabaseMessage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MessageRepositoryImpl(
    private val repositoryProvider: RepositoryProvider,
    private val databaseMessagesMapper: Mapper<MessageResponse, DatabaseMessage>
) : BaseRepository(repositoryProvider),
    MessageRepository {

    override fun getMessages(sparkQuery: AppQuery): Observable<List<DatabaseMessage>> {
        return getRemoteDataProvider()
            .getMessages(sparkQuery)
            .flatMap { messages ->
                val mappedMessages = messages.map { databaseMessagesMapper.map(it) }
                getLocalDataProvider().setMessages(mappedMessages)
                Observable.just(mappedMessages)
            }.onErrorResumeNext { err: Throwable ->
                getLocalDataProvider().getMessages().toObservable()
            }
    }

    override fun getMessagesFromDB(): Single<List<DatabaseMessage>> {
        return getLocalDataProvider().getMessages()
    }

    override fun updateMessage(messageResponse: DatabaseMessage) {
        getLocalDataProvider().updateMessage(messageResponse)
        return getRemoteDataProvider().updateMessage(databaseMessagesMapper.mapReverse(messageResponse))
    }

    override fun addNewMessage(messageResponse: DatabaseMessage): Observable<DatabaseMessage> {
        return getRemoteDataProvider()
            .addNewMessage(databaseMessagesMapper.mapReverse(messageResponse))
            .flatMap { Observable.just(databaseMessagesMapper.map(it)) }
    }

    override fun updateMessageList(databaseMessage: List<DatabaseMessage>): Completable {
        val messagesResponse = databaseMessage.map { databaseMessagesMapper.mapReverse(it) }
        return getRemoteDataProvider().updateMessageList(messagesResponse)
    }

}