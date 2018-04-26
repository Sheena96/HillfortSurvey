package org.wit.hillforts.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_activity.*
import org.jetbrains.anko.startActivity
import org.wit.hillfortsurvey.R


class HillfortSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activity)
        enterButton.setOnClickListener{
           startActivity<HillfortListActivity>()
        }
    }
}