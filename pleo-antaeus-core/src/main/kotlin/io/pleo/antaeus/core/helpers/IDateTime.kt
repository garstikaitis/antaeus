package io.pleo.antaeus.core.helpers

import java.time.LocalDateTime

interface IDateTime {
    fun isFirstDayOfTheMonth() : Boolean{
        return LocalDateTime.now().dayOfMonth == 26
    }
}