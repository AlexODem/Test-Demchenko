package com.example.sparktestdemchenko.data.datasource.repository.message

import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.model.AppQuery
import io.reactivex.Observable

interface MessageRepository {

    fun getMessages(sparkQuery: AppQuery): Observable<List<MessageResponse>>

    fun updateMessage(messageResponse: MessageResponse)

    fun addNewMessage(messageResponse: MessageResponse): Observable<MessageResponse>

}