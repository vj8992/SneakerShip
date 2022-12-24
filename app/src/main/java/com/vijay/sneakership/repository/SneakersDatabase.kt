package com.vijay.sneakership.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vijay.sneakership.R
import com.vijay.sneakership.model.SneakersModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SneakersModel::class], version = 1, exportSchema = false)
abstract class SneakersDatabase: RoomDatabase(){

  abstract fun getSneakersModelDao(): SneakersModelDao

  companion object {
    private const val SNEAKERS_DB = "sneakers.db"

    @Volatile
    private var instance: SneakersDatabase? = null

    fun getInstance(context: Context): SneakersDatabase =
      instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }

    private fun buildDatabase(context: Context) =
      Room.databaseBuilder(context.applicationContext, SneakersDatabase::class.java, SNEAKERS_DB)
        .addCallback(object : RoomDatabase.Callback(){
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let {
              CoroutineScope(Dispatchers.IO).launch {
                val array = context.resources.openRawResource(R.raw.sneakers).bufferedReader().use {
                  it.readText()
                }
                val gson = Gson()
                val listType = object : TypeToken<List<SneakersModel>>() {}.type
                val sneakersList: List<SneakersModel> = gson.fromJson(array, listType)
                instance!!.getSneakersModelDao().insertAll(sneakersList)
              }
            }
          }
        })
        .build()
  }
}