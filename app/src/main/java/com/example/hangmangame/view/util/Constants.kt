package com.example.hangmangame.view.util
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    // URL BASE para las API a utilizar | Deben terminar en /
    const val BASE_URL = "https://www.serverbpw.com/cm/2023-1/"
    const val LOGTAG = "LOGS"

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}