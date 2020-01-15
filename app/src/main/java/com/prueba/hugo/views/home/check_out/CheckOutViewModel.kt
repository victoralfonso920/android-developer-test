package com.prueba.hugo.views.home.check_out

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mobilesv.mor.db.DtaVehicleEntity
import com.prueba.hugo.App
import com.prueba.hugo.R
import com.prueba.hugo.repository.DataRepository
import com.prueba.hugo.tools.Cons
import com.prueba.hugo.tools.SingleLiveEvent
import io.realm.RealmResults
import kotlin.math.floor

/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class CheckOutViewModel(private val repository: DataRepository) : ViewModel() {

    var imageRes = R.drawable.car_air
    var progress = 85.0f

    val gotoback = SingleLiveEvent<Boolean>()
    val gotoPay = SingleLiveEvent<String>()


    var textIdCar = MutableLiveData<String>()
    var textTime = MutableLiveData<String>()
    var textError = MutableLiveData<String>()
    val editTextContent = Observer<String> { onTextChanged(it) }
    val editTextTime = Observer<String> { onTextChanged(it) }


    fun onTextChanged(it: String) {
        // Some code
    }

    fun checkOutParking() {
        if (textIdCar.value!!.isNotEmpty()) {
            UpdateTime()
        } else {
            textError.postValue(App.context.getString(R.string.text_error_plate))
        }
    }

    init {
        textIdCar.postValue("")
        textIdCar.observeForever(editTextContent)
        textTime.observeForever(editTextTime)
    }

    override fun onCleared() {
        textIdCar.removeObserver(editTextContent)
        textTime.removeObserver(editTextTime)
    }

    fun Gotoback() {
        gotoback.postValue(true)
    }

    fun getDataCar(id: String) {
        val dataCar = repository.getDataCarsId(id)
        if (dataCar.isNotEmpty()) {
            textIdCar.postValue(dataCar[0]?.idCar ?: "")
            textTime.postValue(dataCar[0]?.time_start.toString())
        }
    }

    fun UpdateTime() {
        val data = repository.getDataCarsId(textIdCar.value!!)
        if (data.isNotEmpty()) {
            val dtaVehicle = DtaVehicleEntity()
            dtaVehicle.idCar = data[0]!!.idCar
            dtaVehicle.user = data[0]!!.user
            dtaVehicle.time_start = data[0]!!.time_start
            dtaVehicle.time_end = System.currentTimeMillis()
            try {
                repository.createData(dtaVehicle)
            } catch (e: Exception) {
            }
            generateDialog()
        } else {
            textError.postValue("No existe vehiculo asociado a la placa")
        }
    }

    fun generateDialog() {
        if (repository.getDataCars().size > 0) {
            textError.postValue("Se ha registrado salida del vehiculo")
            checkUsaerData()
        } else {
            textError.postValue("Error al registrar salida del vehiculo")
        }
    }

    fun checkUsaerData() {
        val data = repository.getDataCarsId(textIdCar.value!!)
        if (data.isNotEmpty()) {
            val minutes = Math.abs(data[0]!!.time_end!! - data[0]!!.time_start!!)
            var timeInminutes = floor((minutes.toDouble()/1000)/60).toInt()
            var timeTotal = 0

            when (data[0]!!.user) {
                Cons.OFICIAL -> {
                    timeTotal = (data[0]!!.time_total!! + timeInminutes)
                    UpdateFullTime(data, timeTotal)
                }
                Cons.NO_RESIDENTE -> {
                    gotoPay.postValue(textIdCar.value!!)
                }
                Cons.RESIDENTE -> {
                    timeTotal = (data[0]!!.time_total!! + timeInminutes)
                    UpdateFullTime(data, timeTotal)
                }
            }

        }

    }

    fun UpdateFullTime(
        data: RealmResults<DtaVehicleEntity>,
        timeTotal: Int
    ) {

        if (data.isNotEmpty()) {
            val dtaVehicle = DtaVehicleEntity()
            dtaVehicle.idCar = data[0]!!.idCar
            dtaVehicle.user = data[0]!!.user
            dtaVehicle.time_start = data[0]!!.time_start
            dtaVehicle.time_end = data[0]!!.time_end
            dtaVehicle.time_total = timeTotal
            try {
                repository.createData(dtaVehicle)
            } catch (e: Exception) {
            }
        }
    }

}