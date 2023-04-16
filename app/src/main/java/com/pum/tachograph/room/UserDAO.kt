package com.pum.tachograph.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {

    @Insert
    fun insert (user: User)

    @Update
    fun update (user: User)

    @Delete
    fun delete (user: User)

    @Query("SELECT * FROM user_table")
    fun getAllUsers():LiveData<List<User>>
    @Query("DELETE FROM user_table")
    fun deleteAllRows()
}