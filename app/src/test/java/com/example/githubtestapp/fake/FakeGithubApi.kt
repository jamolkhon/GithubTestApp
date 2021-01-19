package com.example.githubtestapp.fake

import com.example.githubtestapp.api.GithubApi
import com.example.githubtestapp.api.GithubUser
import io.reactivex.Single

class FakeGithubApi(
  private val page1: List<GithubUser>,
  private val page2: List<GithubUser>
) : GithubApi {

  override fun getUsers(since: Long): Single<List<GithubUser>> = when (since) {
    0L -> Single.just(page1)
    page1.last().id -> Single.just(page2)
    page2.last().id -> Single.just(emptyList())
    else -> throw IllegalArgumentException()
  }
}
