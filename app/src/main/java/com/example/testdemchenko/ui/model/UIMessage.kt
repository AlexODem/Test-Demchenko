package com.example.testdemchenko.ui.model

import java.io.Serializable

data class UIMessage(
    var key: String?,
    val date: Long?,
    val from: String?,
    val subject: String?,
    val preview: String?,
    var read: Boolean,
    var deleted: Boolean,
    var wasChangedOffline: Boolean = false
) : Serializable