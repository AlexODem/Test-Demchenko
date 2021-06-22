package com.example.testdemchenko.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testdemchenko.data.util.doOnIoSubscribeOnMain
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.domain.usecase.AddNewMessageUseCase
import com.example.testdemchenko.domain.usecase.GetMessagesListUseCase
import com.example.testdemchenko.domain.usecase.UpdateMessageUseCase
import com.example.testdemchenko.ui.model.UIMessage
import com.example.testdemchenko.domain.usecase.UpdateFromDatabaseUseCase

class MessagesViewModel(
    private val getMessagesListUseCase: GetMessagesListUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val addNewMessageUseCase: AddNewMessageUseCase,
    private val updateFromDatabaseUseCase: UpdateFromDatabaseUseCase,
) : BaseViewModel() {

    private val _messages = MutableLiveData<List<UIMessage>>()
    private val _newMessage = MutableLiveData<UIMessage>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isPageLoading = MutableLiveData<Boolean>()

    val messages: LiveData<List<UIMessage>>
        get() = _messages

    val newMessage: LiveData<UIMessage>
        get() = _newMessage

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val isPageLoading: LiveData<Boolean>
        get() = _isPageLoading


    fun chekUpdateAndLoadList() {
        updateFromDatabaseUseCase.readFromDatabase()
            .doOnSubscribe {
                _isLoading.postValue(true)
            }
            .subscribe(
                ::updateMessages,
                ::onError
            )
            .addToClearedDisposable()
    }

    private fun updateMessages(it: List<DatabaseMessage>) {
        updateFromDatabaseUseCase.updateFromDatabase(it).subscribe {
            getMessageList()
        }.addToClearedDisposable()
    }

    fun getMessageList() {
        getMessagesListUseCase.execute()
            .doOnIoSubscribeOnMain()
            .doOnSubscribe {
                _isLoading.postValue(true)
            }
            .doAfterTerminate {
                _isLoading.postValue(false)
            }
            .subscribe(
                ::onSuccess,
                ::onError
            ).addToClearedDisposable()
    }

    fun loadNextPage() {
        getMessagesListUseCase.execute()
            .doOnIoSubscribeOnMain()
            .doOnSubscribe {
                _isPageLoading.postValue(true)
            }
            .doAfterTerminate {
                _isPageLoading.postValue(false)
            }
            .subscribe(
                ::onSuccess,
                ::onError
            ).addToClearedDisposable()
    }


    fun updateMessage(message: UIMessage) {
        updateMessageUseCase.execute(UpdateMessageUseCase.Param(message))
    }

    fun addNewMessage() {
        addNewMessageUseCase.execute()
            .doOnIoSubscribeOnMain()
            .subscribe(
                ::showNewMessage,
                ::onError
            )
            .addToClearedDisposable()
    }

    private fun showNewMessage(message: UIMessage) {
        _newMessage.postValue(message)
    }

    private fun onSuccess(messages: List<UIMessage>) {
        _messages.postValue(messages)
        _isLoading.postValue(false)
    }

    private fun onError(throwable: Throwable) {

    }
}