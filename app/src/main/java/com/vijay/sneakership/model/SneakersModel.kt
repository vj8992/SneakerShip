package com.vijay.sneakership.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sneakers")
data class SneakersModel(
  @PrimaryKey
  val id: Int,
  val brand: String,
  val colorway: String,
  val gender: String,
  val imageUrl: String,
  val releaseDate: String,
  val retailPrice: Int,
  val styleId: String,
  val shoe: String,
  val name: String,
  val title: String,
  val year: String,
)