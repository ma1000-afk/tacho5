package com.pum.tachograph

import androidx.lifecycle.ViewModel
import com.pum.tachograph.room.User

val user1= User(name = "Pepe",
    surname = "Frog")

val user2= User(name = "Pepe2",
    surname = "Frog2")

private val lista= listOf(user1,user2)

interface Server{
    fun loadDataFromDb():List<User>
}

class MainViewModel :ViewModel(),Server{
    var fraga:Int= 0
    private  var user: User?=null
    override fun loadDataFromDb(): List<User> {
        return lista
    }
    fun setUser(user: User){
        this.user=user
    }
    fun getUser()=user
}