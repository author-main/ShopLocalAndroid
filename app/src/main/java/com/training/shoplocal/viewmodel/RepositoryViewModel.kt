package com.training.shoplocal.viewmodel

import androidx.lifecycle.ViewModel
import com.training.shoplocal.repository.Repository

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    fun getRepository() = repository
    fun getPassword()   = repository.passwordState.getPassword()
}