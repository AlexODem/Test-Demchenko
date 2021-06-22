package com.example.testdemchenko.domain.usecase

import com.example.testdemchenko.data.datasource.repository.message.MessageRepository
import com.example.testdemchenko.domain.model.PageInfo
import com.example.testdemchenko.domain.model.AppQuery
import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.domain.usecase.base.ObservableUseCase
import com.example.testdemchenko.domain.util.MessagesListUtils
import com.example.testdemchenko.ui.model.UIMessage
import io.reactivex.Observable

class GetMessagesListUseCase(
    private val repository: MessageRepository,
    private val mapper: Mapper<DatabaseMessage, UIMessage>
) : ObservableUseCase<List<UIMessage>> {

    private val pageInfo = PageInfo()

    override fun execute(): Observable<List<UIMessage>> {
        val sparkQuery = if (pageInfo.getLastItem() != null) {
            AppQuery(pageInfo.getLastItem(), PageInfo.LIMIT)
        } else {
            AppQuery(limit = PageInfo.LIMIT)
        }

        return repository.getMessages(sparkQuery)
            .flatMap { messages ->
                if (pageInfo.isEndOfList().not()) {
                    val lastItem = messages[messages.size - 1]
                    pageInfo.setLastItem(lastItem.key)
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