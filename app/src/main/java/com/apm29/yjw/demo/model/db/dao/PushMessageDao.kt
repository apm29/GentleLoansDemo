package com.apm29.yjw.demo.model.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.apm29.yjw.demo.model.db.entity.PushMessage


@Dao
interface PushMessageDao {
    @Query("SELECT * FROM push_message")
    fun getAll(): List<PushMessage>

    @Query("SELECT * FROM push_message WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<PushMessage>


    @Insert
    fun insertAll(vararg users: PushMessage)

    @Delete
    fun delete(user: PushMessage)
}