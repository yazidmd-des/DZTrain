package com.yzd.dztrain
import java.io.Serializable
data class SearchResult(
    val id: String,
    val depTime: String,
    val arrTime: String,
    val from: String,
    val to: String,
    val stops: List<Stop>,
    val isHoliday: Boolean,
    val lineName: String,
    val fullTrainStops: List<Stop> = emptyList() // Store original stops here for the "Show All" feature
) : Serializable