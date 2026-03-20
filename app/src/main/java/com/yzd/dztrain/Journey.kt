package com.yzd.dztrain

import java.io.Serializable

sealed class Journey : Serializable {
    data class Direct(
        val train: SearchResult,
        val totalDuration: String,
        val durationMins: Int,
        val arrivalAbsMins: Int
    ) : Journey()

    data class Connection(
        val firstTrain: SearchResult,
        val secondTrain: SearchResult,
        val transferStation: String,
        val totalDuration: String,
        val totalDurationMins: Int,
        val arrivalAbsMins: Int
    ) : Journey()

    // Helper to get raw minutes from "HH:mm" for sorting
    fun getDepartureMinutes(): Int {
        val timeStr = when (this) {
            is Direct -> train.depTime
            is Connection -> firstTrain.depTime
        }
        return timeStr.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
    }


    // ADDED: Helper to get final arrival minutes for sorting
    fun getArrivalMinutes(): Int {
        val timeStr = when (this) {
            is Direct -> train.arrTime
            is Connection -> secondTrain.arrTime
        }
        return timeStr.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
    }

    // Helper to get total trip duration in minutes
    fun getDurationMinutes(): Int {
        return when (this) {
            is Direct -> {
                val dep = train.depTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
                val arr = train.arrTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
                if (arr >= dep) arr - dep else (arr + 1440) - dep // Handles midnight wrap
            }
            is Connection -> {
                val dep = firstTrain.depTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
                val arr = secondTrain.arrTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
                if (arr >= dep) arr - dep else (arr + 1440) - dep
            }
        }
    }
}