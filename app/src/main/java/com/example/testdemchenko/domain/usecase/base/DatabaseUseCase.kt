package com.example.testdemchenko.domain.usecase.base

import com.example.testdemchenko.domain.model.DatabaseMessage
import io.reactivex.Completable
import io.reactivex.Single

interface DatabaseUseCase {

    fun updateFromDatabase(message: List<DatabaseMessage>): Completable

    fun readFromDatabase() : Single<List<DatabaseMessage>>
}