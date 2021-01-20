package com.example.githubtestapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubtestapp.Event
import com.example.githubtestapp.R
import com.example.githubtestapp.api.GithubUser
import com.example.githubtestapp.databinding.FragmentMainBinding
import com.example.githubtestapp.databinding.UserItemBinding
import com.example.githubtestapp.nav
import com.example.githubtestapp.support.addOnReachBottomListener
import com.example.githubtestapp.support.viewBinding
import com.squareup.cycler.Recycler
import com.squareup.cycler.toDataSource
import com.squareup.picasso.Picasso
import com.zhuinden.liveevent.observe
import com.zhuinden.simplestackextensions.fragments.KeyedFragment
import com.zhuinden.simplestackextensions.fragmentsktx.lookup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber

class MainFragment : KeyedFragment(R.layout.fragment_main) {

  private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

  private lateinit var recycler: Recycler<GithubUser>

  private val controller: UsersController by lazy { lookup() }

  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    controller.fetchUsers()
  }

  override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
    super.onViewCreated(v, savedInstanceState)

    recycler = recycler()
    binding.usersView.addItemDecoration(
      DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
    )
    binding.swipeLayout.setOnRefreshListener {
      controller.fetchUsers()
    }
    binding.usersView.addOnReachBottomListener { controller.fetchMoreUsers() }
  }

  private fun recycler(): Recycler<GithubUser> {
    return Recycler.adopt(binding.usersView) {
      row<GithubUser, View> {
        create {
          val userBinding = UserItemBinding.inflate(LayoutInflater.from(requireContext()))
          view = userBinding.root
          bind { item -> bindUser(userBinding, item) }
        }
      }
      extraItem<Boolean, View> {
        create(R.layout.loading_more) {
          val indicator = view.findViewById<TextView>(R.id.loadingIndicator)
          bind { retry ->
            indicator.text = if (retry) getString(R.string.failed_tap_to_retry)
            else getString(R.string.loading_more)
            indicator.setOnClickListener {
              controller.fetchMoreUsers()
            }
          }
        }
      }
      stableId { user -> user.id }
      compareItemsContent { a, b -> a == b }
    }
  }

  private fun bindUser(userBinding: UserItemBinding, user: GithubUser) {
    Picasso.get().load(user.avatarUrl).into(userBinding.avatarView)
    userBinding.titleView.text = user.login
    userBinding.subtitleView.text = user.id.toString()
    userBinding.root.setOnClickListener {
      nav.goToDetailScreen(user.id)
    }
  }

  override fun onStart() {
    super.onStart()

    disposables += controller.users
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { users ->
        recycler.update {
          data = users.toDataSource()
        }
      }

    disposables += controller.states
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { state ->
        Timber.e(state.toString())
        binding.swipeLayout.isRefreshing = state.loading
        recycler.update {
          extraItem = if (state.endReached || state.loading) null else state.loadingMoreError != null
        }
      }

    controller.events.observe(this, { event ->
      Timber.e(event.toString())
      if (event is Event.ConnectionProblem) {
        Toast.makeText(requireContext(), getString(R.string.connection_problem), Toast.LENGTH_SHORT).show()
      }
    })
  }

  override fun onStop() {
    super.onStop()
    disposables.clear()
  }
}
