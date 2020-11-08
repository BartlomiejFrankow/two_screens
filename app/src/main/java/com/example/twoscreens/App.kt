package com.example.twoscreens

import android.app.Application
import com.example.twoscreens.modules.firebaseModule
import com.example.twoscreens.modules.viewModelModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAndroidThreeTen()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                    firebaseModule
                )
            )
        }
    }

    protected open fun initAndroidThreeTen() = AndroidThreeTen.init(this)

}