package ru.dimagor555.factcard.data.file

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class File(
    @PrimaryKey val id: Long,
    val name: String,
    val createTime: Long,
)
