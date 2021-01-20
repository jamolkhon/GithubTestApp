package com.example.githubtestapp

import com.example.githubtestapp.api.GithubApi
import com.example.githubtestapp.support.DefaultSchedulerProvider
import com.example.githubtestapp.support.SchedulerProvider
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Binds
  abstract fun schedulerProvider(schedulerProvider: DefaultSchedulerProvider): SchedulerProvider

  @Module
  companion object {

    @Provides
    @Singleton
    @JvmStatic
    fun githubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun retrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
      .baseUrl("https://api.github.com/")
      .client(client)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()

    @Provides
    @Singleton
    @JvmStatic
    fun httpClient(): OkHttpClient = OkHttpClient.Builder()
      .callTimeout(5, TimeUnit.SECONDS)
      .build()

    @Provides
    @Singleton
    @JvmStatic
    fun moshi(): Moshi = Moshi.Builder().build()
  }
}
