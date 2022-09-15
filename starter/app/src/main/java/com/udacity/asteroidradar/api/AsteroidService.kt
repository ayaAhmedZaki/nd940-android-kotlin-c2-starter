package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.main.ImageOfDayResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


 object Network {
    // Configure retrofit to parse JSON and use coroutines
    fun getRetrofitService(): OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)

        return httpClient

    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
       // .addConverterFactory(GsonConverterFactory.create())
        .client(getRetrofitService().build())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val devbytes = retrofit.create(AsteroidService::class.java)
}

interface AsteroidService {

    @GET("neo/rest/v1/feed")
    fun getAsteroidData(@Query("start_date") startDate: String ,
                        @Query("end_date") endDate: String ,
                        @Query("api_key") apiKey: String
    ): Deferred<ResponseBody>


    @GET("planetary/apod")
    fun getimageOfDay(@Query("api_key") apiKey: String): Deferred<ImageOfDayResponse>

}