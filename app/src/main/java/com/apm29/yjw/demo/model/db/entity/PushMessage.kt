package com.apm29.yjw.demo.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "push_message")
class PushMessage {
    @PrimaryKey
    private val uid: Int = 0

    @ColumnInfo(name = "user_id")
    private val userId: Int = 0

    @ColumnInfo(name = "agent_id")
    private val agentId: Int = 0

    @ColumnInfo(name = "jump")
    private val jump: String? = null

    @ColumnInfo(name = "param")
    private val param: String? = null
}