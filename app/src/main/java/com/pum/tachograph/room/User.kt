package com.pum.tachograph.room



import androidx.annotation.DrawableRes

//data class User(val name:String, val surname:String, val tech:String){}


import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity (tableName="user_table")
data class User (var name: String, var surname: String) {

    @PrimaryKey(autoGenerate = true)
    var user_id : Int = 0
}