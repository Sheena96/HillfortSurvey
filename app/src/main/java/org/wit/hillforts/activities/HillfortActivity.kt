package org.wit.hillforts.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.remove
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.helpers.readImage
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillfortsurvey.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            info("Select image")
        }


        if (intent.hasExtra("hillfort_edit")) {
            edit = true;
            btnAdd.setText(R.string.save_hillfort)
            hillfort = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            hillfortTownland.setText(hillfort.townland)
            county.setText(hillfort.county)
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.change_hillfort_image)
            }
        }

        btnAdd.setOnClickListener() {
            hillfort.townland = hillfortTownland.text.toString()
            hillfort.county = county.text.toString()

            if (edit) {
                app.hillforts.update(hillfort.copy())
                setResult(201)
                finish()
            } else {
                if (hillfort.townland.isNotEmpty()) {
                    app.hillforts.create(hillfort.copy())
                    setResult(200)
                    finish()
                } else {
                    toast(R.string.enter_hillfort_townland)
                }
            }
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }


        hillfortLocation.setOnClickListener {
            startActivity (intentFor<MapsActivity>())
        }
    }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_hillfort, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.item_cancel -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                R.id.item_delete -> {
                    app.hillforts.delete(hillfort)
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
            return super.onOptionsItemSelected(item)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                IMAGE_REQUEST -> {
                    if (data != null) {
                        hillfort.image = data.getData().toString()
                        hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    }
                }
            }
        }
    }



