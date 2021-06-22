package com.example.sparktestdemchenko.data.datasource.repository.base

import com.example.sparktestdemchenko.data.datasource.provider.base.RepositoryProvider

abstract class BaseRepository(private val repositoryProvider: RepositoryProvider) {

    fun getRemoteDataProvider() = repositoryProvider.getRemoteDataProvider()

    fun getLocalDataProvider() = repositoryProvider.getLocalDataProvider()
}