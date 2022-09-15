package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.domain.Asteroid

//@JsonClass(generateAdapter = true)
//data class NetworkVideoContainer(val videos: List<NetworkVideo>)

/**
 * Videos represent a devbyte that can be played.
 */
//@JsonClass(generateAdapter = true)
//data class NetworkVideo(
//    val id: Long, val codename: String, val closeApproachDate: String,
//    val absoluteMagnitude: Double, val estimatedDiameter: Double,
//    val relativeVelocity: Double, val distanceFromEarth: Double,
//    val isPotentiallyHazardous: Boolean,
//    val closedCaptions: String?)
//
///**
// * Convert Network results to database objects
// */
//fun NetworkVideoContainer.asDomainModel(): List<Asteroid> {
//    return videos.map {
//        Asteroid(id = it.id,
//            codename = it.codename,
//            closeApproachDate = it.closeApproachDate,
//            absoluteMagnitude = it.absoluteMagnitude,
//            estimatedDiameter = it.estimatedDiameter,
//            relativeVelocity = it.relativeVelocity,
//            distanceFromEarth = it.distanceFromEarth,
//            isPotentiallyHazardous = it.isPotentiallyHazardous)
//    }
//}

//create an extension function that converts from data transfer objects to database objects:
fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
       DatabaseAsteroid(
           id = it.id,
           codename = it.codename,
           closeApproachDate = it.closeApproachDate,
           absoluteMagnitude = it.absoluteMagnitude,
           estimatedDiameter = it.estimatedDiameter,
           relativeVelocity = it.relativeVelocity,
           distanceFromEarth = it.distanceFromEarth,
           isPotentiallyHazardous = it.isPotentiallyHazardous
       )
    }.toTypedArray()
}
