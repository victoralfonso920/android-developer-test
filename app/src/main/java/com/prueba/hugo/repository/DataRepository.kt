package com.prueba.hugo.repository

import com.google.gson.Gson
import com.mobilesv.mor.db.DtaVehicleEntity
import com.prueba.hugo.App
import com.prueba.hugo.db.clearDataFromId
import com.prueba.hugo.db.create
import com.prueba.hugo.db.getData
import com.prueba.hugo.db.getDataFromId
import com.prueba.hugo.model.OptionsModel
import io.realm.RealmResults

/* Created by
 victor Hernandez
 on 14/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class DataRepository(private val gson: Gson) {

    private var optionsData : OptionsModel? = null
    private var menuHomeData : OptionsModel? = null

    suspend fun getOptions() =
        optionsData?.let {
            it
        } ?: generateOptions()


    fun generateOptions() : OptionsModel{
        val jsonfile: String = App.context.assets.open("question.json").bufferedReader().use {it.readText()}
        return gson.fromJson(jsonfile, OptionsModel::class.java)
    }


    suspend fun getMenuOptions() =
        menuHomeData?.let {
            it
        } ?: generateMenuOptions()


    fun generateMenuOptions() : OptionsModel{
        val jsonfile: String = App.context.assets.open("menu.json").bufferedReader().use {it.readText()}
        return gson.fromJson(jsonfile, OptionsModel::class.java)
    }

    fun getFisrtItem():String{
        return optionsData?.options?.get(0)?.name ?: ""
    }

    //create data
     fun createData(dtaVehicle: DtaVehicleEntity) {
            dtaVehicle.create()
    }

    //getdata()
     fun getDataCars( ): RealmResults<DtaVehicleEntity> {
        return DtaVehicleEntity().getData()
    }
    //getDataId
    fun getDataCarsId(id:String): RealmResults<DtaVehicleEntity> {
        return DtaVehicleEntity().getDataFromId(id)
    }

    fun deleteDataFromId(id:String):Boolean{
        return DtaVehicleEntity().clearDataFromId(id)
    }


}