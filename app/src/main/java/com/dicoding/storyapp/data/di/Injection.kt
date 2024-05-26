package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.pref.UserPreferences
import com.dicoding.storyapp.data.pref.datastore
import com.dicoding.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.datastore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}