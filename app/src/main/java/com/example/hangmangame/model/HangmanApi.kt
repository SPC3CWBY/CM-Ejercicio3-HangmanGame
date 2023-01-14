package com.example.hangmangame.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface HangmanApi {
    //Endpoints
    @GET
    fun getWords(
        @Url url: String?
    ): Call<Words>
}