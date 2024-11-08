package com.fin.technicalapp

import android.app.Application
import com.fin.technicalapp.core.di.networkModule
import com.fin.technicalapp.core.di.repositoryModule
import com.fin.technicalapp.core.di.stashModule
import com.fin.technicalapp.di.viewModelModule

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TechnicalApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TechnicalApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    viewModelModule,
                    stashModule,
                )
            )
        }
    }
}