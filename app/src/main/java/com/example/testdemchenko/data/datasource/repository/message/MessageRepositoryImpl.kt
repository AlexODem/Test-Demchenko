package com.example.sparktestdemchenko.data.datasource.repository.message

import com.example.sparktestdemchenko.data.datasource.provider.base.RepositoryProvider
import com.example.sparktestdemchenko.data.datasource.repository.base.BaseRepository
import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.model.AppQuery
import io.reactivex.Observable

class MessageRepositoryImpl(repositoryProvider: RepositoryProvider) : BaseRepository(repositoryProvider), MessageRepository {

    override fun getMessages(sparkQuery: AppQuery): Observable<List<MessageResponse>> {
        return getRemoteDataProvider().getMessages(sparkQuery)
    }

    override fun updateMessage(messageResponse: MessageResponse) {
        return getRemoteDataProvider().updateMessage(messageResponse)
    }

    override fun addNewMessage(messageResponse: MessageResponse): Observable<MessageResponse> {
        return getRemoteDataProvider().addNewMessage(messageResponse)
    }

}