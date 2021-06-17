package com.example.sparktestdemchenko.domain.mapper.base

interface Mapper<T,R> {
    fun map(source: T): R
    fun mapReverse(source: R): T
}