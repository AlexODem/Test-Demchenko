package com.example.sparktestdemchenko.data.remote

import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.model.AppQuery
import io.reactivex.Observable

interface RemoteDataProvider {
    fun getMessages(sparkQuery: AppQuery): Observable<List<MessageResponse>>

    fun updateMessage(message: MessageResponse)

    fun addNewMessage(message: MessageResponse) : Observable<MessageResponse>
}