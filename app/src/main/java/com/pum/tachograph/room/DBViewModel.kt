package com.pum.tachograph.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

class DBViewModel(application: Application) : AndroidViewModel(application){
    private var userRepository:UserRepository=UserRepository(application)
    private var allUser:Deferred<LiveData<List<User>>> = userRepository.getAllUserAsync()
    fun insertUser(user: User){
        userRepository.insertUser(user)
        Log.d("iiii","Jestem")
    }
    fun updateUser(user: User){
        userRepository.updateUser(user)
    }
    fun deleteUser(user: User){
        userRepository.deleteUser(user)
    }
    fun getAllUser():LiveData<List<User>> = runBlocking { allUser.await() }
    fun deleteAllRows(){userRepository.deleteAllRows()}
}