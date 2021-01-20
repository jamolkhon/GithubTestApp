package com.example.githubtestapp

import com.example.githubtestapp.api.GithubUser
import com.example.githubtestapp.fake.FakeGithubApi
import com.example.githubtestapp.fake.FakeUsers
import com.example.githubtestapp.main.MainViewState
import com.example.githubtestapp.main.UsersController
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class UsersControllerTest {

  @Test
  fun fetching() {
    val githubApi = FakeGithubApi(
      listOf(FakeUsers.user1, FakeUsers.user2),
      listOf(FakeUsers.user3, FakeUsers.user4)
    )
    val scheduler = TestScheduler()
    val controller = UsersController(githubApi, TestSchedulerProvider(scheduler))

    val usersObserver = SimpleObserver<List<GithubUser>>()
    controller.users.subscribe(usersObserver)
    assertEquals(emptyList<List<GithubUser>>(), usersObserver.value)

    val stateObserver = SimpleObserver<MainViewState>()
    controller.states.subscribe(stateObserver)
    assertEquals(MainViewState(false), stateObserver.value)

    controller.fetchUsers()
    assertEquals(MainViewState(true), stateObserver.value)
    scheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
    assertEquals(listOf(FakeUsers.user1, FakeUsers.user2), usersObserver.value)
    assertEquals(MainViewState(false), stateObserver.value)

    controller.fetchMoreUsers()
    assertEquals(MainViewState(true), stateObserver.value)
    scheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
    assertEquals(listOf(FakeUsers.user1, FakeUsers.user2, FakeUsers.user3, FakeUsers.user4), usersObserver.value)
    assertEquals(MainViewState(false), stateObserver.value)
  }

  @Test
  fun selecting() {
    val githubApi = FakeGithubApi(
      listOf(FakeUsers.user1, FakeUsers.user2),
      listOf(FakeUsers.user3, FakeUsers.user4)
    )
    val scheduler = TestScheduler()
    val controller = UsersController(githubApi, TestSchedulerProvider(scheduler))

    val observer = SimpleObserver<GithubUser>()
    controller.selectedUser.subscribe(observer)
    assertNull(observer.value)

    controller.selectUser(FakeUsers.user1.id)
    assertNull(observer.value)

    controller.fetchUsers()
    scheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

    controller.selectUser(FakeUsers.user1.id)
    assertEquals(FakeUsers.user1, observer.value)

    controller.selectUser(FakeUsers.user2.id)
    assertEquals(FakeUsers.user2, observer.value)
  }
}
