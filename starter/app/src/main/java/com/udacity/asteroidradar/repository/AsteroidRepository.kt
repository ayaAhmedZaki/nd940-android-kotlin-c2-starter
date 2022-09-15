package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroid: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid(getCurrentDate()!!)) {
            it.asDomainModel()
        }

    val asteroidToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidToday(getCurrentDate()!!)) {
            it.asDomainModel()
        }

    val asteroidSaved: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getSavedAsteroid()) {
            it.asDomainModel()
        }

    suspend fun refreshList() {
//        Log.d("RepoData" , "test ${getCurrentDate()}")
//        Log.d("RepoData" , "dateee ${getNextDate(getCurrentDate())}")

        //val listData : List<Asteroid>
        withContext(Dispatchers.IO) {
            val startDate = getCurrentDate()
            val endDate = getNextDate(startDate)
            val responseData =
                Network.devbytes.getAsteroidData(startDate!!, endDate!!, API_KEY).await().string()
            val listData = parseAsteroidsJsonResult(JSONObject(responseData))

            database.asteroidDao.insertAll(*listData.asDatabaseModel()) //Note the asterisk * is the spread operator.
            // It allows you to pass in an array to a function that expects varargs.
        }

    }

}
