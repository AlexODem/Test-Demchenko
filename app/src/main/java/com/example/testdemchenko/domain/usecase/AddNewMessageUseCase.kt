package com.example.sparktestdemchenko.domain.usecase

import com.example.sparktestdemchenko.data.datasource.repository.message.MessageRepository
import com.example.sparktestdemchenko.domain.mapper.base.Mapper
import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.usecase.base.ObservableUseCase
import com.example.sparktestdemchenko.domain.util.RandomMessageGenerator
import com.example.sparktestdemchenko.ui.model.UIMessage
import io.reactivex.Observable

class AddNewMessageUseCase(private val repository: MessageRepository,
                           private val mapper: Mapper<MessageResponse, UIMessage>
) : ObservableUseCase<UIMessage> {

    override fun execute(): Observable<UIMessage> {
        val generatedMessage = RandomMessageGenerator.generate()
        return repository.addNewMessage(mapper.mapReverse(generatedMessage))
            .flatMap { Observable.just(mapper.map(it))  }
    }

}