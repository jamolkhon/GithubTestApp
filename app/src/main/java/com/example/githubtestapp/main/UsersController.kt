package com.example.githubtestapp.main

import com.example.githubtestapp.api.GithubApi
import com.example.githubtestapp.api.GithubUser
import com.example.githubtestapp.support.EventEmitterConsumer
import com.example.githubtestapp.support.SchedulerProvider
import com.jakewharton.rxrelay2.BehaviorRelay
import com.zhuinden.commandqueue.CommandQueue
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.simplestack.ScopedServices
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class UsersController @Inject constructor(
    private val githubApi: GithubApi,
    private val schedulers: SchedulerProvider
) : ScopedServices.Registered {

  private val _users = BehaviorRelay.createDefault<List<GithubUser>>(emptyList())
  val users: Observable<List<GithubUser>> = _users

  private val _selectedUser = BehaviorRelay.create<GithubUser>()
  val selectedUser: Observable<GithubUser> = _selectedUser

  private val _state = BehaviorRelay.createDefault(MainViewState())
  val state: Observable<MainViewState> = _state

  private val _events = EventEmitterConsumer<Any>()
  val events: EventSource<Any> = _events

  private val disposables = CompositeDisposable()

  fun fetchUsers() {
    disposables += githubApi.getUsers(0)
      .subscribeOn(schedulers.io())
      .observeOn(schedulers.ui())
      .doOnSubscribe {
        _state.accept(MainViewState(true))
      }
      .doFinally {
        _state.accept(MainViewState(false))
      }
      .subscribe(_users, _events)
  }

  fun fetchMoreUsers() {
    val since = _users.value?.lastOrNull()?.id ?: 0
    disposables += githubApi.getUsers(since)
      .subscribeOn(schedulers.io())
      .observeOn(schedulers.ui())
      .doOnSubscribe {
        _state.accept(MainViewState(true))
      }
      .doFinally {
        _state.accept(MainViewState(false))
      }
      .map { users -> _users.value!!.plus(users) }
      .subscribe(_users, _events)
  }

  fun selectUser(id: Long) {
    _users.value
      ?.find { user -> user.id == id }
      ?.let { user -> _selectedUser.accept(user) }
  }

  override fun onServiceRegistered() {
  }

  override fun onServiceUnregistered() {
    disposables.clear()
  }
}
