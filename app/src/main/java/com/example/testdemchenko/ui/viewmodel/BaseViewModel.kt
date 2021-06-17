package com.example.sparktestdemchenko.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseViewModel : ViewModel() {
    private val onClearedDisposable = CompositeDisposable()

    override fun onCleared() {
        onClearedDisposable.clear()
        super.onCleared()
    }

    protected fun Disposable.addToClearedDisposable(): Disposable {
        onClearedDisposable.add(this)
        return this
    }
}