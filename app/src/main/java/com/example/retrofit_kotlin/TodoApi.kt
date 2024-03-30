package com.example.retrofit_kotlin

import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {

    @GET("/todos")
    suspend fun getTodo() : Response<List<DataClass>>
}