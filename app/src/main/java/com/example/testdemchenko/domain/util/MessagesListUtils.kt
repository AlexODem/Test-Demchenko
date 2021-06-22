package com.example.testdemchenko.domain.util

import com.example.testdemchenko.domain.model.DatabaseMessage

class MessagesListUtils(private var list: List<DatabaseMessage>) {

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