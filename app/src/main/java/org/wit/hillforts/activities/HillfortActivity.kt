package org.wit.hillforts.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillforts.models.HillfortModel
import org.wit.hillfortsurvey.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)

        btnAdd.setOnClickListener() {
           hillfort.title = hillfortTitle.text.toString()
            if (hillfort.title.isNotEmpty()) {
                info("add Button Pressed: $hillfortTitle")
            }
            else {
                toast ("Please Enter a Hillfort")
            }
        }


    }
}
