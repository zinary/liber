package com.zinary.liber

import android.app.Application
import android.content.Context

class Liber : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: Liber
        fun getContext(): Context {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
    }
}