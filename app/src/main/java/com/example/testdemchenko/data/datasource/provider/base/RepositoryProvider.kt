package com.example.sparktestdemchenko.data.datasource.provider.base

import com.example.testdemchenko.data.local.LocalDataProvider
import com.example.testdemchenko.data.remote.RemoteDataProvider

interface RepositoryProvider {
    fun getRemoteDataProvider() : RemoteDataProvider

    fun getLocalDataProvider() : LocalDataProvider
}