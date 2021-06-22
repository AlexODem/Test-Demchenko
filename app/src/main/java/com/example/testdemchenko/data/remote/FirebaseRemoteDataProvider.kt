package com.example.testdemchenko.data.remote

import com.example.testdemchenko.domain.model.MessageResponse
import com.example.testdemchenko.domain.model.AppQuery
import com.example.testdemchenko.domain.mapper.base.Mapper
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject


class FirebaseRemoteDataProvider(
    private val firebaseDatabase: FirebaseDatabase,
    private val queryMapper: Mapper<AppQuery, Query>
) : RemoteDataProvider {

    override fun getMessages(sparkQuery: AppQuery): Observable<List<MessageResponse>> {
        val behaviorSubject = PublishSubject.create<List<MessageResponse>>()
        val messages = mutableListOf<MessageResponse>()

        val firebaseQuery = queryMapper.map(sparkQuery)

        firebaseQuery.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach {
                val element = it.getValue(MessageResponse::class.java)
                if (element != null) {
                    element.setId(it.key.toString())
                    messages.add(element)
                }
            }

            behaviorSubject.onNext(messages)
            behaviorSubject.onComplete()

        }.addOnFailureListener {
            behaviorSubject.onError(it)
        }

        return behaviorSubject
    }

    override fun updateMessage(message: MessageResponse) {
        val reference = firebaseDatabase.reference
        val messageValue = message.toMap()
        val messageUpdates = hashMapOf<String, Any>(
            "${message.key}" to messageValue,
        )
        reference.updateChildren(messageUpdates)
    }

    override fun updateMessageList(messages: List<MessageResponse>): Completable {
        val completableSubject = CompletableSubject.create()

        val reference = firebaseDatabase.reference
        val messageUpdates = hashMapOf<String, Any>()

        messages.forEach { message ->
            val messageValue = message.toMap()
            messageUpdates["${message.key}"] = messageValue
        }

        reference.updateChildren(messageUpdates)
            .addOnSuccessListener {
                completableSubject.onComplete()
            }
            .addOnFailureListener {
                completableSubject.onError(it)
            }

        if (messages.isEmpty())
            completableSubject.onComplete()

        return completableSubject
    }

    override fun addNewMessage(message: MessageResponse) : Observable<MessageResponse> {
        firebaseDatabase.reference.child(message.key).setValue(message, null, null)

        val behaviorSubject = PublishSubject.create<MessageResponse>()
        firebaseDatabase.reference.child(message.key).get().addOnSuccessListener {
            val messageResponse = it.getValue(MessageResponse::class.java)
            if (messageResponse != null) {
                behaviorSubject.onNext(messageResponse)
                behaviorSubject.onComplete()
            }
        }
        return behaviorSubject
    }
}






