package io.pleo.antaeus.core.helpers

import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException

interface ExceptionWrapper {
    fun raise(e: Exception) : String {
        return when (e) {
            is CurrencyMismatchException -> "Customer does not support given currency"
            is CustomerNotFoundException -> "Customer was not found, make sure that customer exists"
            is NetworkException -> "A network error has occured. Please try again"
            else -> "Something went wrong ${e.message}"
        }
    }
}