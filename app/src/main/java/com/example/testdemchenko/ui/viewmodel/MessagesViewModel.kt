package com.example.sparktestdemchenko.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sparktestdemchenko.data.util.doOnIoSubscribeOnMain
import com.example.sparktestdemchenko.domain.usecase.AddNewMessageUseCase
import com.example.sparktestdemchenko.domain.usecase.GetMessagesListUseCase
import com.example.sparktestdemchenko.domain.usecase.UpdateMessageUseCase
import com.example.sparktestdemchenko.ui.model.UIMessage

class MessagesViewModel(
    private val getMessagesListUseCase: GetMessagesListUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val addNewMessageUseCase: AddNewMessageUseCase
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
    }

    private fun onError(throwable: Throwable) {

    }


}