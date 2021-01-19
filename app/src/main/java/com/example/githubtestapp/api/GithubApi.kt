package com.example.githubtestapp.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

  @GET("users")
  fun getUsers(@Query("since") since: Long): Single<List<GithubUser>>
}
