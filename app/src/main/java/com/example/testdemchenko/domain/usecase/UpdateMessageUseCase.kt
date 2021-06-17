package com.example.sparktestdemchenko.domain.usecase

import com.example.sparktestdemchenko.data.datasource.repository.message.MessageRepository
import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.mapper.base.Mapper
import com.example.sparktestdemchenko.domain.usecase.base.VoidUseCaseWithParam
import com.example.sparktestdemchenko.ui.model.UIMessage

class UpdateMessageUseCase(
    private val repository: MessageRepository,
    private val mapper: Mapper<MessageResponse, UIMessage>
) : VoidUseCaseWithParam<UpdateMessageUseCase.Param> {

    override fun execute(param: Param) {
        val uiMessage = param.uiMessage
        repository.updateMessage(mapper.mapReverse(uiMessage))
    }

    data class Param(val uiMessage: UIMessage)
}