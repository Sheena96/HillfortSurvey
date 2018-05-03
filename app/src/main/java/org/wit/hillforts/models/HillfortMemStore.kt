package org.wit.hillforts.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    suspend override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.townland = hillfort.townland
            foundHillfort.county = hillfort.county
            foundHillfort.image = hillfort.image
            //foundHillfort.date = hillfort.date
        }
    }

    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    internal fun logAll() {
        hillforts.forEach{ info( "${it}")}
    }
}