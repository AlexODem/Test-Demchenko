package com.example.testdemchenko.domain.usecase.base

import io.reactivex.Observable

interface ObservableUseCase<T> {
    fun execute(): Observable<T>
}