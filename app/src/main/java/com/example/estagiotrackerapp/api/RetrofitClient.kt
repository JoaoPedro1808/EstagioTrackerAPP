package com.example.estagiotrackerapp.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = "https://devmobile-ap2-estagiotracker-api.onrender.com/"

    private val clientColdDown = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: EstagioTrackerAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientColdDown)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EstagioTrackerAPI::class.java)
    }
}