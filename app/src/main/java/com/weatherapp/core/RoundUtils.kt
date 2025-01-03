package com.weatherapp.core

import kotlin.math.ceil
import kotlin.math.floor

fun roundDoubleToIntMin(doubleValue: Double): Int {
    return floor(doubleValue).toInt()
}
fun roundDoubleToIntMax(doubleValue: Double): Int {
    return ceil(doubleValue).toInt()
}