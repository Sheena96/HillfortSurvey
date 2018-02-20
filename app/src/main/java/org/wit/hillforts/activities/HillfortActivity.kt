package org.wit.hillforts.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillfortsurvey.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        btnAdd.setOnClickListener() {
           hillfort.townland = hillfortTownland.text.toString()
            hillfort.county = county.text.toString()
            if (hillfort.townland.isNotEmpty()) {
                app.hillforts.create(hillfort.copy())
                //info("add Button Pressed: $hillfortTownland")
                //app.hillforts.findAll().forEach() { info("add Button Pressed: ${it}")}
                app.hillforts.findAll().forEach{ info("add Button Pressed: ${it}") }
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
            else {
                toast ("Please Enter a Townland")
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
