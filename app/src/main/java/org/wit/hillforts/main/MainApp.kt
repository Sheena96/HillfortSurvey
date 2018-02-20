package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.models.HillfortModel

class MainApp : Application(), AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    override fun onCreate() {
        super.onCreate()
        info("Hillfort Survey started")
        //hillforts.add(HillfortModel("One", "About one..."))
        //hillforts.add(HillfortModel("Two", "About two..."))
        //hillforts.add(HillfortModel("Three", "About three..."))
    }
}