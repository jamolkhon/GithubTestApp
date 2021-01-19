package com.example.githubtestapp

import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add

class ScopeConfiguration(private val appComponent: AppComponent) : ScopedServices {

  override fun bindServices(serviceBinder: ServiceBinder) {
    when (serviceBinder.scopeTag) {
      Scopes.USERS.name -> {
        serviceBinder.add(appComponent.mainController())
      }
    }
  }
}
