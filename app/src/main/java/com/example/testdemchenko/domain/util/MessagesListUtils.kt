package com.example.sparktestdemchenko.domain.util

import com.example.sparktestdemchenko.domain.model.MessageResponse

class MessagesListUtils(private var list: List<MessageResponse>) {

    fun sort(): MessagesListUtils {
        list = list.sortedBy { it.date }.reversed()
        return this
    }

    fun hideDeleteItems() : MessagesListUtils {
        list = list.filter { it.deleted.not() }
        return this
    }

    fun getTransformedList() = list
}