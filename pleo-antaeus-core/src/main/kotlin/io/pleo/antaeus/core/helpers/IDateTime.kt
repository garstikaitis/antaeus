package io.pleo.antaeus.core.helpers

import java.time.LocalDateTime

interface IDateTime {
    fun isFirstDayOfTheMonth() : Boolean{
        if(System.getenv("TEST_ENV").toBoolean()) {
            return true
        }
        return LocalDateTime.now().dayOfMonth == System.getenv("CURRENT_DAY").toInt()
    }
}