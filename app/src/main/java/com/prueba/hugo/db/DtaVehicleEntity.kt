package com.mobilesv.mor.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DtaVehicleEntity:RealmObject() {
    @PrimaryKey
    var idCar: String? = ""
    var user: String? = ""
    var time_start: Long? = 0
    var time_end: Long? = 0
    var time_total:Int? = 0
}


