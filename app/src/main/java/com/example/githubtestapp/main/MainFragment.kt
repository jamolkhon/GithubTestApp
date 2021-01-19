package com.example.githubtestapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
    binding.usersView.addOnReachBottomListener { controller.fetchMoreUsers() }
  }

  private fun recycler(): Recycler<GithubUser> {
    return Recycler.adopt(binding.usersView) {
      row<GithubUser, View> {
        create(R.layout.user_item) {
          val userBinding = UserItemBinding.inflate(LayoutInflater.from(requireContext()))
          view = userBinding.root
          bind { item -> bindUser(userBinding, item) }
        }
      }
      extraItem<Any, View> {
        create(R.layout.loading_more) {}
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

    disposables += controller.state
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { state ->
        recycler.update {
          extraItem = if (state.loading) Unit else null
        }
      }

    controller.events.observe(this, { event ->
      Toast.makeText(requireContext(), event.toString(), Toast.LENGTH_LONG).show()
    })
  }

  override fun onStop() {
    super.onStop()
    disposables.clear()
  }
}
