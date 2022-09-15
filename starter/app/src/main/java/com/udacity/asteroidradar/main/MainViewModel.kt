package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import okhttp3.internal.notify

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

enum class AsteroidFilterStatus { WEEK, TODAY, SAVED }


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData<AsteroidApiStatus>()

    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val _filterStatus = MutableLiveData<AsteroidFilterStatus>()

    val filterStatus: LiveData<AsteroidFilterStatus>
        get() = _filterStatus


    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)


    private val _imageData = MutableLiveData<ImageOfDayResponse>()

    val imageData: LiveData<ImageOfDayResponse>
        get() = _imageData

//    private val _asteroidlist = MutableLiveData<List<Asteroid>>()
//
//    val asteroidlist: LiveData<List<Asteroid>>
//        get() = _asteroidlist


    init {
        getImage()
        getDataList()
        //  _asteroidlist.value = asteroidRepository.asteroid
        //  getDb()


    }
    var asteroidTodaylist = asteroidRepository.asteroidToday
    var asteroidlist = asteroidRepository.asteroid
    var asteroidSavedlist = asteroidRepository.asteroidSaved
//    private fun getDb() {
//        _data.value = asteroidRepository.asteroid
//    }



    private fun getImage() {
        viewModelScope.launch {
            try {
                _imageData.value = Network.devbytes.getimageOfDay(API_KEY).await()
            } catch (e: Exception) {
                Log.d("MAINViewModel", "error")

            }
        }

    }

    private fun getDataList() {

        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                Log.d("MAINViewModel", "Done")
                asteroidRepository.refreshList()
                _status.value = AsteroidApiStatus.DONE
                // _asteroidData.value = asteroidRepository.refreshList()
                //   Log.d("MAINViewModel" , "TEst ${parseAsteroidsJsonResult(JSONObject(responseData)).size}")

                //_status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                Log.d("MAINViewModel", "ERROR")
                _status.value = AsteroidApiStatus.ERROR
                // _asteroidData.value = ArrayList()
            }
        }
    }

    fun setFilterValue(filterStatus: AsteroidFilterStatus) {
        Log.d("FilterStatus", "datd ${filterStatus}")
        _filterStatus.value = filterStatus
        Log.d("FilterStatus", "after ${_filterStatus.value}")
        when (filterStatus) {
            AsteroidFilterStatus.TODAY -> {
                asteroidTodaylist = asteroidRepository.asteroidToday
                Log.d("FilterStatus", "list ${asteroidTodaylist.value?.size}")
            }
            AsteroidFilterStatus.SAVED -> {
                asteroidSavedlist = asteroidRepository.asteroidSaved
                Log.d("FilterStatus", "list ${asteroidSavedlist.value?.size}")
            }
            else -> {
                asteroidlist = asteroidRepository.asteroid
                Log.d("FilterStatus", "list ${asteroidlist.value?.size}")
            }
        }
        // setData(filterStatus)


    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}