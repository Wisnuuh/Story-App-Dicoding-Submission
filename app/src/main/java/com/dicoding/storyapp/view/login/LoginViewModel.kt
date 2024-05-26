package com.dicoding.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.response.LoginResponse
import kotlinx.coroutines.launch
import com.dicoding.storyapp.data.response.Result

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, pass: String): LiveData<Result<LoginResponse>> {
        return repository.login(email, pass)
    }
}