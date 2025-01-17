package com.udacity.asteroidradar.main

data class ImageOfDayResponse(
    val date: String,
    val explanation: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)