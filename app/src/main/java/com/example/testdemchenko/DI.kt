package com.example.sparktestdemchenko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.sparktestdemchenko.data.remote.FirebaseRemoteDataProvider
import com.example.sparktestdemchenko.data.remote.RemoteDataProvider
import com.example.sparktestdemchenko.data.datasource.provider.base.RepositoryProvider
import com.example.sparktestdemchenko.data.datasource.repository.message.MessageRepository
import com.example.sparktestdemchenko.data.datasource.repository.message.MessageRepositoryImpl
import com.example.sparktestdemchenko.domain.mapper.FirebaseQueryMapper
import com.example.sparktestdemchenko.domain.mapper.MessageMapper
import com.example.sparktestdemchenko.domain.mapper.base.Mapper
import com.example.sparktestdemchenko.domain.model.MessageResponse
import com.example.sparktestdemchenko.domain.model.AppQuery
import com.example.sparktestdemchenko.domain.usecase.AddNewMessageUseCase
import com.example.sparktestdemchenko.domain.usecase.GetMessagesListUseCase
import com.example.sparktestdemchenko.domain.usecase.UpdateMessageUseCase
import com.example.sparktestdemchenko.ui.model.UIMessage
import com.example.sparktestdemchenko.ui.viewmodel.MessagesViewModel
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// Remote
private val firebaseDatabase
    get() = Firebase.database

private val remoteDataProvider: RemoteDataProvider
    get() = FirebaseRemoteDataProvider(firebaseDatabase, sparkQueryMapper)


// Mappers
private val sparkQueryMapper: Mapper<AppQuery, Query>
    get() = FirebaseQueryMapper(firebaseDatabase.reference)

private val messageMapper: Mapper<MessageResponse, UIMessage>
    get() = MessageMapper()


// Repository
private val repositoryProvider: RepositoryProvider
    get() = object : RepositoryProvider {
        override fun getRemoteDataProvider(): RemoteDataProvider = remoteDataProvider
    }

private fun getMessageRepository(): MessageRepository {
    return MessageRepositoryImpl(repositoryProvider)
}

// UseCase
private fun getMessageListUseCase() = GetMessagesListUseCase(getMessageRepository(), messageMapper)

private fun updateMessageUseCase() = UpdateMessageUseCase(getMessageRepository(), messageMapper)

private fun addNewMessageUseCase() = AddNewMessageUseCase(getMessageRepository(), messageMapper)


// ViewModel
class ViewModelFactory(
    private val getMessagesListUseCase: GetMessagesListUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val addNewMessageUseCase: AddNewMessageUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessagesViewModel::class.java)) {
            return MessagesViewModel(getMessagesListUseCase, updateMessageUseCase, addNewMessageUseCase) as T
        }
        throw IllegalArgumentException("Unknown view model class requested")
    }
}

fun getMessageViewModel(storeOwner: ViewModelStoreOwner) = ViewModelProvider(
    storeOwner,
    ViewModelFactory(getMessageListUseCase(), updateMessageUseCase(), addNewMessageUseCase())
)[MessagesViewModel::class.java]