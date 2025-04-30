package com.example.library

import android.app.Application
import com.example.library.data.AppContainer
import com.example.library.data.DefaultAppContainer

class LibraryApplication : Application() {

    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}