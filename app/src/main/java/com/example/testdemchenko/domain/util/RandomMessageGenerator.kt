package com.example.sparktestdemchenko.domain.util

import com.example.testdemchenko.ui.model.UIMessage
import org.joda.time.DateTime
import kotlin.random.Random

object RandomMessageGenerator {

    private const val SECOND = 1000

    fun generate(): UIMessage {
        return UIMessage(
            ((DateTime.now().millis / SECOND) * -1).toString(),
            DateTime.now().millis / SECOND,
            "Lorem Ipsum: ${Random.nextInt()}",
            "${Random.nextInt()} Lorem Ipsum Subject",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            false,
            false
        )
    }

}