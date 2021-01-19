package com.example.githubtestapp.detail

import android.os.Bundle
import android.view.View
import com.example.githubtestapp.R
import com.example.githubtestapp.api.GithubUser
import com.example.githubtestapp.databinding.FragmentDetailBinding
import com.example.githubtestapp.main.UsersController
import com.example.githubtestapp.support.viewBinding
import com.squareup.picasso.Picasso
import com.zhuinden.simplestackextensions.fragments.KeyedFragment
import com.zhuinden.simplestackextensions.fragmentsktx.lookup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class DetailFragment : KeyedFragment(R.layout.fragment_detail) {

  private val binding: FragmentDetailBinding by viewBinding(FragmentDetailBinding::bind)

  private val controller: UsersController by lazy { lookup() }

  private val disposables = CompositeDisposable()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
  }

  override fun onStart() {
    super.onStart()
    controller.selectUser(getKey<DetailScreen>().userId)
    disposables += controller.selectedUser
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(::showUser)
  }

  private fun showUser(user: GithubUser) {
    Picasso.get().load(user.avatarUrl).into(binding.avatarView)
    binding.loginView.text = user.login
  }

  override fun onStop() {
    super.onStop()
    disposables.clear()
  }
}
