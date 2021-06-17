package com.example.sparktestdemchenko.domain.model

data class PageInfo(
    private var isEndList: Boolean = false,
    private var lastItem: MessageResponse? = null
) {

    fun setEndOfList() {
        isEndList = true
    }

    fun setLastItem(lastItem: MessageResponse) {
        this.lastItem = lastItem
    }

    fun getLastItem() = lastItem

    fun isEndOfList() = isEndList

    companion object {
        const val LIMIT = 10
    }
}