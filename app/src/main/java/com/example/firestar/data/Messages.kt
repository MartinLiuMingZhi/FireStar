package com.example.firestar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
class Messages(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Long = 1,

    @ColumnInfo(name = "sender_id")
    val senderId:Long,

    @ColumnInfo(name = "receiver_id")
    val receiverId:Long,

    @ColumnInfo(name = "context")
    val context: String,

//    @ColumnInfo(name = "timestamp")
//    val timestamp:

    val type: Int
)