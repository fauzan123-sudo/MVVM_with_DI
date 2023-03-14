package com.example.kud.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kud.data.model.CheckOut
import com.example.kud.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckOutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(item : CheckOut)

    @Query("Select * From checkOut")
    fun getAllData(): LiveData<List<CheckOut>>

    @Update
    suspend fun updateItem(user: CheckOut)

    @Query("Delete from checkOut")
    suspend fun deleteAllItem()

    @Delete
    suspend fun delete(item: CheckOut)

}