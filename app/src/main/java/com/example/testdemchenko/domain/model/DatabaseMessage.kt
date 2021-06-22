package com.example.testdemchenko.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import java.io.Serializable
import java.util.*

@Entity(tableName = "messages")
data class DatabaseMessage(
    @PrimaryKey
    var key: String,
    val date: Long?,
    val from: String?,
    val subject: String?,
    val preview: String?,
    var read: Boolean,
    var deleted: Boolean,
    var wasChangedOffline: Boolean = false
)
