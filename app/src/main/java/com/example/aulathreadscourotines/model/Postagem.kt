package com.example.aulathreadscourotines.model

import android.accessibilityservice.GestureDescription
import com.google.gson.annotations.SerializedName

data class Postagem(
    @SerializedName("body")
    val description: String,
    val id: Int,
    val title: String,
    val userId: Int
)