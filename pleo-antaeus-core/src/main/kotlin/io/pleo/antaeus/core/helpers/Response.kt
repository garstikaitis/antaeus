package io.pleo.antaeus.core.helpers

data class Response(val statusCode: Int = 200, val message: String = "Success", val data: List<Any>) {
    fun sendResponse() : Map<Int, String> {
        var map: MutableMap<Int, String> = mutableMapOf()
        map.plus(Pair("statusCode", statusCode))
        map.plus(Pair("message", message))
        map.plus(Pair("data", data))
        return map
    }
}