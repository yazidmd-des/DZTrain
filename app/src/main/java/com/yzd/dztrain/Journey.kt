package com.yzd.dztrain
sealed class Journey {
    data class Direct(val train: SearchResult) : Journey()
    data class Connection(
        val firstTrain: SearchResult,
        val secondTrain: SearchResult,
        val transferStation: String,
        val totalDuration: String
    ) : Journey()
}