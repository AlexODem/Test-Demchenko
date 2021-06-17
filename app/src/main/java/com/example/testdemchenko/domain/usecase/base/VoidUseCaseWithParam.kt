package com.example.sparktestdemchenko.domain.usecase.base

interface VoidUseCaseWithParam<P> {
    fun execute(param: P)
}