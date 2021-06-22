package com.example.testdemchenko.domain.mapper

import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.domain.model.MessageResponse

class DatabaseMessagesMapper: Mapper<MessageResponse, DatabaseMessage> {

    override fun map(source: MessageResponse): DatabaseMessage {
        return  DatabaseMessage(source.key, source.date, source.from, source.subject, source.preview, source.read, source.deleted)
    }

    override fun mapReverse(source: DatabaseMessage): MessageResponse {
        return MessageResponse(source.key ?: "", source.date, source.from, source.subject, source.preview, source.read, source.deleted)
    }
}