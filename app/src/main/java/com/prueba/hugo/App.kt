package com.prueba.hugo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.prueba.hugo.di.inject_module.InjectModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: App private set
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = instance.applicationContext
        startKoin {
            androidLogger()
            androidContext(this@App)
            // load modules here
            modules(InjectModule.injectModule)
        }

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name("My_parking.realm")
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}