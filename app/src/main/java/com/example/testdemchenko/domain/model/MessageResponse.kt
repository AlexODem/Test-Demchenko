package com.example.sparktestdemchenko.domain.model

import com.google.firebase.database.Exclude
import java.io.Serializable
import java.util.*

data class MessageResponse(
    var key: String,
    val date: Long?,
    val from: String?,
    val subject: String?,
    val preview: String?,
    var read: Boolean,
    var deleted: Boolean,
) : Serializable {
    constructor() : this("", null, null, null, null, true, false)

    fun setId(key: String) {
        this.key = key
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "date" to date,
            "deleted" to deleted,
            "from" to from,
            "preview" to preview,
            "read" to read,
            "subject" to subject
        )
    }
}
