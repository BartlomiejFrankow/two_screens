package com.example.twoscreens

import android.app.Application
import com.example.network.networkModule
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.context.startKoin

@FlowPreview
@ExperimentalCoroutinesApi
@Suppress("unused")
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAndroidThreeTen()

        startKoin {
            modules(
                listOf(
                    viewModelModule,
                    networkModule
                )
            )
        }
    }

    protected open fun initAndroidThreeTen() = AndroidThreeTen.init(this)

}