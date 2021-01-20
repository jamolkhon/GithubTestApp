package com.example.githubtestapp.main

data class MainViewState(
  val loading: Boolean = false,
  val loadingError: Throwable? = null,
  val loadingMore: Boolean = false,
  val loadingMoreError: Throwable? = null,
  val endReached: Boolean = false
) {

  fun loading() = copy(loading = true, loadingError = null, endReached = false)

  fun loadingDone(error: Throwable? = null) = copy(loading = false, loadingError = error)

  fun loadingMore() = copy(loadingMore = true, loadingMoreError = null)

  fun loadingMoreDone(error: Throwable? = null, endReached: Boolean = false) =
    copy(loadingMore = false, loadingMoreError = error, endReached = endReached)

  fun isLoading() = loading || loadingMore
}
