package com.example.sparktestdemchenko.domain.usecase

import com.example.sparktestdemchenko.data.datasource.repository.message.MessageRepository
import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.model.PageInfo
import com.example.sparktestdemchenko.domain.model.AppQuery
import com.example.sparktestdemchenko.domain.mapper.base.Mapper
import com.example.sparktestdemchenko.domain.usecase.base.ObservableUseCase
import com.example.sparktestdemchenko.domain.util.MessagesListUtils
import com.example.sparktestdemchenko.ui.model.UIMessage
import io.reactivex.Observable

class GetMessagesListUseCase(
    private val repository: MessageRepository,
    private val mapper: Mapper<MessageResponse, UIMessage>
) : ObservableUseCase<List<UIMessage>> {

    private val pageInfo = PageInfo()

    override fun execute(): Observable<List<UIMessage>> {
        val sparkQuery = if (pageInfo.getLastItem() != null) {
            AppQuery(pageInfo.getLastItem()?.key, PageInfo.LIMIT)
        } else {
            AppQuery(limit = PageInfo.LIMIT)
        }

        return repository.getMessages(sparkQuery)
            .flatMap { messages ->
                if (pageInfo.isEndOfList().not()) {
                    val lastItem = messages[messages.size - 1]
                    pageInfo.setLastItem(lastItem)
                }

                if (messages.isEmpty() || messages.size < PageInfo.LIMIT) {
                    pageInfo.setEndOfList()
                }

                val sort = MessagesListUtils(messages).run {
                    sort()
                    hideDeleteItems()
                    getTransformedList()
                }

                val uiMessages = sort.map { mapper.map(it) }
                Observable.just(uiMessages)
            }
    }
}