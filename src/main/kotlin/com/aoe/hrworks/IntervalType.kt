package com.aoe.hrworks

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class IntervalType(val apiValue: String) {
    DAYS("days"),
    WEEKS("weeks"),
    MONTH("month")
}

class IntervalTypeAdapter : TypeAdapter<IntervalType>() {
    override fun write(out: JsonWriter?, value: IntervalType?) {
        when (value) {
            null -> out!!.nullValue()
            else -> out!!.value(value.apiValue)
        }
    }

    override fun read(`in`: JsonReader?): IntervalType = throw UnsupportedOperationException("not used")

}