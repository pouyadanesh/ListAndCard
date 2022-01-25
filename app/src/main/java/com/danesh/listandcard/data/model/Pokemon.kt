package com.danesh.listandcard.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey
    val id : String,
    val name: String?,
    val hp: String?,
    val artist: String?,
    var isLike: Boolean?,
    val imageUrl: String?
): Parcelable
