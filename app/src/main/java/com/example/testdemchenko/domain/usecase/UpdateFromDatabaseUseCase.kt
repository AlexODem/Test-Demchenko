package com.example.testdemchenko.domain.usecase

import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.data.datasource.repository.message.MessageRepository
import com.example.testdemchenko.domain.usecase.base.DatabaseUseCase
import io.reactivex.Completable
import io.reactivex.Single

class UpdateFromDatabaseUseCase(private val repository: MessageRepository) : DatabaseUseCase {

    override fun updateFromDatabase(message: List<DatabaseMessage>): Completable {
        val filter = message.filter { it.wasChangedOffline }
        return repository.updateMessageList(filter)
    }

    override fun readFromDatabase(): Single<List<DatabaseMessage>> {
        return repository.getMessagesFromDB()
    }

}