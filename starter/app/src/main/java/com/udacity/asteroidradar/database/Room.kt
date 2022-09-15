package com.udacity.asteroidradar.database

import android.content.Context
import android.provider.BaseColumns
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.api.getCurrentDate


@Dao
interface AsteroidDao {

    @Query("select * from DatabaseAsteroid WHERE closeApproachDate >= :currentDate " +
            "ORDER BY closeApproachDate ASC")
    fun getAsteroid(currentDate : String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from DatabaseAsteroid WHERE closeApproachDate = :currentDate " +
            "ORDER BY closeApproachDate ASC")
    fun getAsteroidToday(currentDate : String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from DatabaseAsteroid " +
            "ORDER BY closeApproachDate ASC")
    fun getSavedAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid").build()
        }
    }
    return INSTANCE
}