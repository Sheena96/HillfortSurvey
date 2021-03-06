package org.wit.hillfort.room

import android.arch.persistence.room.Room
import android.content.Context
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.models.HillfortModel

class HillfortStoreRoom(context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
                .fallbackToDestructiveMigration()
                .build()
        dao = database.hillfortDao()
    }

    override suspend fun findAll(): List<HillfortModel> {
        val deferredHillforts = bg {
            dao.findAll()
        }
        val hillforts = deferredHillforts.await()
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        bg {
            dao.create(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        bg {
            dao.update(hillfort)
        }
    }

    override fun delete(hillfort: HillfortModel) {
        bg {
            dao.deleteHillfort(hillfort)
        }
    }

    override suspend fun findById(id: Long): HillfortModel? {
        val deferredHillfort = bg {
            dao.findById(id)
        }
        val hillfort = deferredHillfort.await()
        return hillfort
    }
}