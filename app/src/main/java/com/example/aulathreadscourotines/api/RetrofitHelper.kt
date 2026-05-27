package com.example.aulathreadscourotines.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        val retrofit = Retrofit.Builder()
            //.baseUrl("https://viacep.com.br/")
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())//json or xml
            .build()
    }
}