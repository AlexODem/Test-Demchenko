package com.example.testdemchenko.domain.usecase

import com.example.testdemchenko.data.datasource.repository.message.MessageRepository
import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.domain.usecase.base.VoidUseCaseWithParam
import com.example.testdemchenko.ui.model.UIMessage

class UpdateMessageUseCase(
    private val repository: MessageRepository,
    private val mapper: Mapper<DatabaseMessage, UIMessage>
) : VoidUseCaseWithParam<UpdateMessageUseCase.Param> {

    override fun execute(param: Param) {
        val uiMessage = param.uiMessage
        repository.updateMessage(mapper.mapReverse(uiMessage))
    }

    data class Param(val uiMessage: UIMessage)
}