package com.example.testdemchenko.domain.model

data class PageInfo(
    private var isEndList: Boolean = false,
    private var lastItem: String? = null
) {

    fun setEndOfList() {
        isEndList = true
    }

    fun setLastItem(lastItem: String) {
        this.lastItem = lastItem
    }

    fun getLastItem() = lastItem

    fun isEndOfList() = isEndList

    companion object {
        const val LIMIT = 10
    }
}