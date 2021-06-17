package com.example.sparktestdemchenko.data.datasource.provider.base

import com.example.sparktestdemchenko.data.remote.RemoteDataProvider

interface RepositoryProvider {
    fun getRemoteDataProvider() : RemoteDataProvider
}