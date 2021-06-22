package com.example.testdemchenko.domain.usecase.base

interface VoidUseCaseWithParam<P> {
    fun execute(param: P)
}