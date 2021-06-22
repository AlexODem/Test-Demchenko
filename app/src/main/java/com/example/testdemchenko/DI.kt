package com.example.testdemchenko

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.testdemchenko.data.remote.FirebaseRemoteDataProvider
import com.example.testdemchenko.data.remote.RemoteDataProvider
import com.example.sparktestdemchenko.data.datasource.provider.base.RepositoryProvider
import com.example.testdemchenko.data.datasource.repository.message.MessageRepository
import com.example.testdemchenko.data.datasource.repository.message.MessageRepositoryImpl
import com.example.sparktestdemchenko.domain.mapper.FirebaseQueryMapper
import com.example.sparktestdemchenko.domain.mapper.MessageMapper
import com.example.testdemchenko.domain.mapper.base.Mapper
import com.example.testdemchenko.domain.model.MessageResponse
import com.example.testdemchenko.domain.model.AppQuery
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.domain.usecase.AddNewMessageUseCase
import com.example.testdemchenko.domain.usecase.GetMessagesListUseCase
import com.example.testdemchenko.domain.usecase.UpdateMessageUseCase
import com.example.testdemchenko.ui.model.UIMessage
import com.example.testdemchenko.ui.viewmodel.MessagesViewModel
import com.example.testdemchenko.data.local.AppDatabase
import com.example.testdemchenko.data.local.LocalDataProvider
import com.example.testdemchenko.data.local.RoomDatabaseDataProvider
import com.example.testdemchenko.domain.mapper.DatabaseMessagesMapper
import com.example.testdemchenko.domain.usecase.UpdateFromDatabaseUseCase
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// Remote
private val firebaseDatabase
    get() = Firebase.database

private val remoteDataProvider: RemoteDataProvider
    get() = FirebaseRemoteDataProvider(firebaseDatabase, sparkQueryMapper)

private fun getAppDatabase(context: Context) = AppDatabase.getInstance(context)

fun getLocalDataProvider(context: Context) : RoomDatabaseDataProvider {
    return RoomDatabaseDataProvider(getAppDatabase(context))
}

// Mappers
private val sparkQueryMapper: Mapper<AppQuery, Query>
    get() = FirebaseQueryMapper(firebaseDatabase.reference)

private val messageMapper: Mapper<DatabaseMessage, UIMessage>
    get() = MessageMapper()

private val databaseMessageMapper: Mapper<MessageResponse, DatabaseMessage>
    get() = DatabaseMessagesMapper()


// Repository
private fun getRepositoryProvider(context: Context): RepositoryProvider {
    return object : RepositoryProvider {
        override fun getRemoteDataProvider(): RemoteDataProvider = remoteDataProvider
        override fun getLocalDataProvider(): LocalDataProvider = getLocalDataProvider(context = context)
    }
}

private fun getMessageRepository(context: Context): MessageRepository {
    return MessageRepositoryImpl(getRepositoryProvider(context), databaseMessagesMapper = databaseMessageMapper)
}

// UseCase
private fun getMessageListUseCase(context: Context) = GetMessagesListUseCase(getMessageRepository(context), messageMapper)

private fun updateMessageUseCase(context: Context) = UpdateMessageUseCase(getMessageRepository(context), messageMapper)

private fun addNewMessageUseCase(context: Context) = AddNewMessageUseCase(getMessageRepository(context), messageMapper)

private fun uploadMessagesUseCase(context: Context) = UpdateFromDatabaseUseCase(getMessageRepository(context))


// ViewModel
class ViewModelFactory(
    private val getMessagesListUseCase: GetMessagesListUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val addNewMessageUseCase: AddNewMessageUseCase,
    private val updateFromDatabaseUseCase: UpdateFromDatabaseUseCase,
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessagesViewModel::class.java)) {
            return MessagesViewModel(getMessagesListUseCase, updateMessageUseCase, addNewMessageUseCase, updateFromDatabaseUseCase) as T
        }
        throw IllegalArgumentException("Unknown view model class requested")
    }
}

fun getMessageViewModel(context: Context, storeOwner: ViewModelStoreOwner) = ViewModelProvider(
    storeOwner,
    ViewModelFactory(getMessageListUseCase(context), updateMessageUseCase(context), addNewMessageUseCase(context), uploadMessagesUseCase(context))
)[MessagesViewModel::class.java]