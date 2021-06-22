package com.example.sparktestdemchenko.domain.mapper

import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.ui.model.UIMessage


class MessageMapper : Mapper<DatabaseMessage, UIMessage> {

    override fun map(source: DatabaseMessage): UIMessage {
        return  UIMessage(source.key, source.date, source.from, source.subject, source.preview, source.read, source.deleted, false)
    }

    override fun mapReverse(source: UIMessage): DatabaseMessage {
        return DatabaseMessage(source.key ?: "", source.date, source.from, source.subject, source.preview, source.read, source.deleted, source.wasChangedOffline)
    }

}