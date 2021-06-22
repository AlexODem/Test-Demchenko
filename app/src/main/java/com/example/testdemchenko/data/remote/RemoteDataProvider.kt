package com.example.testdemchenko.data.remote

import com.example.testdemchenko.domain.model.MessageResponse
import com.example.testdemchenko.domain.model.AppQuery
import io.reactivex.Completable
import io.reactivex.Observable

interface RemoteDataProvider {
    fun getMessages(sparkQuery: AppQuery): Observable<List<MessageResponse>>

    fun updateMessage(message: MessageResponse)

    fun updateMessageList(messages: List<MessageResponse>): Completable

    fun addNewMessage(message: MessageResponse) : Observable<MessageResponse>
}