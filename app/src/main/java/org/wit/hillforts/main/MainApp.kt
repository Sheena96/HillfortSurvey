package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.models.HillfortMemStore
import org.wit.hillforts.models.HillfortModel

class MainApp : Application(), AnkoLogger {
    
    val hillforts = HillfortMemStore()

    override fun onCreate() {
        super.onCreate()
        info("Hillfort Survey started")
    }
}