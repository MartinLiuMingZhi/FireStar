package com.example.firestar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Long = 1,

    @ColumnInfo(name = "senderId")
    val senderId:Long,

    @ColumnInfo(name = "receiverId")
    val receiverId:Long,

    @ColumnInfo(name = "context")
    val content: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    val type: Int
)