package com.prueba.hugo.model

import android.os.Parcelable
import com.prueba.hugo.R
import kotlinx.android.parcel.Parcelize

/* Created by
 victor Hernandez
 on 14/01/2020.
 contact : victoralfonso92@yahoo.com
 */
@Parcelize
data class OptionsModel(
    val options: List<Option>? = arrayListOf()
):Parcelable

@Parcelize
data class Option(
    val id: String? = "",
    val name: String? = "",
    val img: Int? = R.drawable.front_car
):Parcelable