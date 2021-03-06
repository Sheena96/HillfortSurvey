package org.wit.hillforts.activities


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location
import org.wit.hillfortsurvey.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.RatingBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.wit.hillfort.helpers.*


class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    lateinit var map: GoogleMap
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    //var location = Location(52.245696, -7.139102, 15f)
    val defaultLocation = Location(52.245696, -7.139102, 15f)
    val locationRequest = createDefaultLocationRequest()

    var textview_date: TextView? = null
    var cal = Calendar.getInstance()

    private lateinit var locationService: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp
        mapView2.onCreate(savedInstanceState);

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        mapView2.getMapAsync {
            map = it
            configureMap()
        }

        chooseImage.setOnClickListener {
            info("Select image")
        }

        val mRatingBar = findViewById<View>(R.id.ratingBar) as RatingBar

        hillfortHere.isEnabled = false

        hillfortHere.setOnClickListener {
            setCurrentLocation()
        }

        locationService = LocationServices.getFusedLocationProviderClient(this)

        if (intent.hasExtra("hillfort_edit")) {
            edit = true;
            hillfort = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            hillfortTownland.setText(hillfort.townland)
            county.setText(hillfort.county)
            hillfortDate.setText(hillfort.date)
            lat.setText(hillfort.lat.toString())
            lng.setText(hillfort.lng.toString())
            mRatingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener {
                ratingBar, v, b -> }
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.change_hillfort_image)
            }
        } else {
            hillfort.lat = defaultLocation.lat
            hillfort.lng = defaultLocation.lng
            hillfort.zoom = defaultLocation.zoom
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        hillfortLocation.setOnClickListener {
            if (hillfort.zoom != 0f) {
                defaultLocation.lat = hillfort.lat
                defaultLocation.lng = hillfort.lng
                defaultLocation.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", defaultLocation), LOCATION_REQUEST)
        }

        //References from the layout file
        textview_date = this.hillfortDate
        textview_date!!.text = "--/--/----"

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        SetDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@HillfortActivity, dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                save()
            }
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun save() {
        hillfort.townland = hillfortTownland.text.toString()
        hillfort.county = county.text.toString()
        hillfort.date = hillfortDate.text.toString()
        //hillfort.lat = location.lat
        //hillfort.lng = location.lng
        hillfort.rating = ratingBar.rating

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

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        val loc = LatLng(hillfort.lat, hillfort.lng)
        val options = MarkerOptions().title(hillfort.townland).position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.zoom))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_hillfort_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    //map.clear()
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    configureMap()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView2.onResume()
        if (!edit) {
            startLocationUpdates()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView2.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (checkLocationPermissions(this)) {
            hillfortHere.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            hillfortHere.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            defaultLocation.lat = it.latitude
            defaultLocation.lng = it.longitude
            hillfort.lat = it.latitude
            hillfort.lng = it.longitude
            configureMap()
        }
    }

    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null && locationResult.locations != null) {
                val l = locationResult.locations.last()
                info("Location Update ${l.latitude} ${l.longitude}")
                lat.setText(l.latitude.toString())
                lng.setText(l.longitude.toString())
                hillfort.lat = l.latitude
                hillfort.lng = l.longitude
                configureMap()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}








