package com.example.sparktestdemchenko.domain.mapper

import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.mapper.base.Mapper
import com.example.sparktestdemchenko.ui.model.UIMessage


class MessageMapper : Mapper<MessageResponse, UIMessage> {

    override fun map(source: MessageResponse): UIMessage {
        return  UIMessage(source.key, source.date, source.from, source.subject, source.preview, source.read, source.deleted)
    }

    override fun mapReverse(source: UIMessage): MessageResponse {
        return MessageResponse(source.key ?: "", source.date, source.from, source.subject, source.preview, source.read, source.deleted)
    }

}