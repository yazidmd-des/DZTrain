package com.yzd.dztrain

data class SearchResult(
    val id: String,
    val depTime: String,
    val arrTime: String,
    val from: String,
    val to: String,
    val stops: List<Stop>,
    val isHoliday: Boolean,
    val lineName: String, // Add this
    val fullTrainStops: List<Stop> = emptyList() // Store original stops here for the "Show All" feature
)