package com.example.githubtestapp.main

import com.example.githubtestapp.Event
import com.example.githubtestapp.api.GithubApi
import com.example.githubtestapp.api.GithubUser
import com.example.githubtestapp.support.EventEmitterConsumer
import com.example.githubtestapp.support.SchedulerProvider
import com.jakewharton.rxrelay2.BehaviorRelay
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.simplestack.ScopedServices
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.io.IOException
import javax.inject.Inject

class UsersController @Inject constructor(
    private val githubApi: GithubApi,
    private val schedulers: SchedulerProvider
) : ScopedServices.Registered {

  private val _users = BehaviorRelay.createDefault<List<GithubUser>>(emptyList())
  val users: Observable<List<GithubUser>> = _users

  private val _selectedUser = BehaviorRelay.create<GithubUser>()
  val selectedUser: Observable<GithubUser> = _selectedUser

  private val _states = BehaviorRelay.createDefault(MainViewState())
  private val state: MainViewState
    get() = _states.value!!
  val states: Observable<MainViewState> = _states

  private val _events = EventEmitterConsumer<Any>()
  val events: EventSource<Any> = _events

  private val disposables = CompositeDisposable()

  fun fetchUsers() {
    if (state.isLoading()) return
    disposables += githubApi.getUsers(0)
      .subscribeOn(schedulers.io())
      .observeOn(schedulers.ui())
      .doOnSubscribe {
        _states.accept(state.loading())
      }
      .doOnSuccess {
        _states.accept(state.loadingDone())
      }
      .doOnError { error ->
        handleError(error)
        _states.accept(state.loadingDone(error))
      }
      .subscribe(_users, {})
  }

  fun fetchMoreUsers() {
    if (state.isLoading()) return
    val since = _users.value?.lastOrNull()?.id ?: 0
    disposables += githubApi.getUsers(since)
      .subscribeOn(schedulers.io())
      .observeOn(schedulers.ui())
      .doOnSubscribe {
        _states.accept(state.loadingMore())
      }
      .doOnSuccess { users ->
        _states.accept(state.loadingMoreDone(endReached = users.isEmpty()))
      }
      .doOnError { error ->
        handleError(error)
        _states.accept(state.loadingMoreDone(error))
      }
      .map { users -> _users.value!!.plus(users) }
      .subscribe(_users, {})
  }

  fun selectUser(id: Long) {
    _users.value
      ?.find { user -> user.id == id }
      ?.let { user -> _selectedUser.accept(user) }
  }

  private fun handleError(t: Throwable) {
    if (t is IOException) {
      _events.accept(Event.ConnectionProblem)
    }
    else {
      _events.accept(Event.UnknownProblem)
    }
  }

  override fun onServiceRegistered() {
  }

  override fun onServiceUnregistered() {
    disposables.clear()
  }
}
