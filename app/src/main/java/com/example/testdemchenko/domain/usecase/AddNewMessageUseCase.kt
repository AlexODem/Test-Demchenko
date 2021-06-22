package com.example.testdemchenko.domain.usecase

import com.example.testdemchenko.data.datasource.repository.message.MessageRepository
import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.domain.usecase.base.ObservableUseCase
import com.example.sparktestdemchenko.domain.util.RandomMessageGenerator
import com.example.testdemchenko.ui.model.UIMessage
import io.reactivex.Observable

class AddNewMessageUseCase(private val repository: MessageRepository,
                           private val mapper: Mapper<DatabaseMessage, UIMessage>
) : ObservableUseCase<UIMessage> {

    override fun execute(): Observable<UIMessage> {
        val generatedMessage = RandomMessageGenerator.generate()
        return repository.addNewMessage(mapper.mapReverse(generatedMessage))
            .flatMap { Observable.just(mapper.map(it))  }
    }

}