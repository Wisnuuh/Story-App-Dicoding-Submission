package com.dicoding.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.UserRepository

class SignupViewModel(private val repository: UserRepository): ViewModel() {

    fun register(name: String, email: String, pass: String) = repository.register(name, email, pass)
}