package com.example.githubtestapp

import com.example.githubtestapp.api.GithubApi
import com.example.githubtestapp.main.MainFragment
import com.example.githubtestapp.main.UsersController
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

  fun mainController(): UsersController

  fun githubApi(): GithubApi
}
