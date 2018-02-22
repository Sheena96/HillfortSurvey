package org.wit.hillforts.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class HillfortModel((var id: Long = 0,
                         var townland: String = "",
                         var county: String = "") : Parcelable
