package org.wit.hillforts.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.wit.hillforts.main.MainApp
import org.wit.hillfortsurvey.R

class HillfortListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

    }
}