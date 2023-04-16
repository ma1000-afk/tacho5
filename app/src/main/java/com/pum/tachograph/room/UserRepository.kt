package com.pum.tachograph.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class UserRepository(application: Application) {
    private  var userDAO:UserDAO

    init {
        val database=UserDatabase
            .getInstace(application.applicationContext)

        userDAO=database!!.userDao()
    }

    fun insertUser (user: User) : Job =
        CoroutineScope(Dispatchers.IO).launch{
        userDAO.insert(user)}

    fun updateUser (user: User) : Job =
        CoroutineScope(Dispatchers.IO).launch {
            userDAO.update(user)}
    fun deleteUser(user: User)=
        CoroutineScope(Dispatchers.IO).launch{userDAO.delete(user)}
//////////////////////

            fun getAllUserAsync(): Deferred<LiveData<List<User>>> =
                CoroutineScope(Dispatchers.IO).async{
                userDAO.getAllUsers()
                }

            fun deleteAllRows()=
                CoroutineScope(Dispatchers.IO).launch {
                userDAO.deleteAllRows()
            }

}