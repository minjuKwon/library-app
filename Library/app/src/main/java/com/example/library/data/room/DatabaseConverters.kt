package com.example.library.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson

class DatabaseConverters {

    @TypeConverter
    fun listToJson(value:List<String>?):String?{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value:String):List<String>?{
        return Gson().fromJson(value, Array<String>::class.java)?.toList()
    }

}