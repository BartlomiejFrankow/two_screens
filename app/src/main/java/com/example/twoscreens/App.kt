package com.example.twoscreens

import android.app.Application
import com.example.twoscreens.module.viewModelModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.core.context.startKoin

@Suppress("unused")
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAndroidThreeTen()

        startKoin {
            modules(viewModelModule)
        }
    }

    protected open fun initAndroidThreeTen() = AndroidThreeTen.init(this)

}