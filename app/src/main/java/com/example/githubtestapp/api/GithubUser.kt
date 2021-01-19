package com.example.githubtestapp.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubUser(
    val id: Long,
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String
)
