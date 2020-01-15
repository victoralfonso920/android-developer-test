package com.prueba.hugo.views.home.pay_bill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mobilesv.mor.db.DtaVehicleEntity
import com.prueba.hugo.App
import com.prueba.hugo.R
import com.prueba.hugo.repository.DataRepository
import com.prueba.hugo.tools.Cons
import com.prueba.hugo.tools.ConvertAmount
import com.prueba.hugo.tools.SingleLiveEvent
import io.realm.RealmResults
import java.util.*
import java.util.concurrent.TimeUnit


/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class PayBillViewModel(private val repository: DataRepository): ViewModel() {



    var imageRes = R.drawable.mercedez
    val gotoback = SingleLiveEvent<Boolean>()



    var textCarId = MutableLiveData<String>()
    var textTime= MutableLiveData<String>()
    var textBill= MutableLiveData<String>()
    var textError= MutableLiveData<String>()
    var textSuccess= MutableLiveData<String>()
    val editTextContent = Observer<String>{onTextChanged(it)}
    val editTextTime = Observer<String>{onTextChanged(it)}
    val editTextBill = Observer<String>{onTextChanged(it)}


    init {
        textCarId.postValue("")
        textCarId.observeForever(editTextContent)
        textTime.observeForever(editTextTime)
    }

    override fun onCleared() {
        textCarId.removeObserver(editTextContent)
        textTime.removeObserver(editTextTime)
        textBill.removeObserver(editTextBill)
    }

    fun onTextChanged(it: String) {
        // Some code
    }


fun payBill(){
    val data = repository.getDataCarsId(textCarId.value!!)
    if(data.isNotEmpty()){
        if(data[0]!!.user!!.equals(Cons.NO_RESIDENTE,true)){
            if(repository.deleteDataFromId(data[0]!!.idCar!!)){
                textSuccess.postValue("Se realizo pago correctamente")
            }else{
                textError.postValue("Hubo un error al realizar el pago")
            }
        }else{
            updateUser(data)
        }
    }

}

    fun updateUser(
            data: RealmResults<DtaVehicleEntity>
        ) {
        if(textCarId.value!!.isNotEmpty()){
            val dtaVehicle = DtaVehicleEntity()
            dtaVehicle.idCar = data[0]!!.idCar
            dtaVehicle.user = data[0]!!.user
            dtaVehicle.time_start = 0
            dtaVehicle.time_end = 0
            dtaVehicle.time_total = 0
            try {
                repository.createData(dtaVehicle)
            } catch (e: Exception) {
            }
        }else{
            textError.postValue("No existe un numero de placa de referencia")
        }

        }

    fun GetDifference(start: Long, end: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = start
        val hour = cal[Calendar.HOUR_OF_DAY]
        val min = cal[Calendar.MINUTE]
        var t = (23 - hour) * 3600000 + (59 - min) * 60000.toLong()
        t = start + t
        var diff = 0
        if (end > t) {
            diff = ((end - t) / TimeUnit.DAYS.toMillis(1)).toInt() + 1
        }
        return diff
    }

    fun checkUsaerData(id:String){
        textCarId.postValue(id)
        val data =   repository.getDataCarsId(id)
        if(data.isNotEmpty()){

            var timeInminutes = GetDifference(data[0]!!.time_start!!,data[0]!!.time_end!!)

            var bill = 0.00
            var timeTotal = 0

            when(data[0]!!.user){
                Cons.OFICIAL->{
                    timeTotal = data[0]!!.time_total!!
                    textTime.postValue(timeTotal.toString())
                    bill = data[0]!!.time_total!! * Cons.BILL_OFICIAL
                    textBill.postValue(bill.ConvertAmount())
                }
                Cons.NO_RESIDENTE ->{
                    bill = timeInminutes * Cons.BILL_NO_RESIDENTE
                    textTime.postValue(timeInminutes.toString())
                    textBill.postValue(bill.ConvertAmount())

                }
                Cons.RESIDENTE ->{
                    textTime.postValue(timeTotal.toString())
                    bill = data[0]!!.time_total!! * Cons.BILL_RESIDENTE
                    textBill.postValue(bill.ConvertAmount())
                }
            }
        }

    }

    fun Gotoback(){
        gotoback.postValue(true)
    }

    fun checkPay() {
        if (textCarId.value!!.isNotEmpty()) {
            checkUsaerData(textCarId.value!!)
        } else {
            textError.postValue(App.context.getString(R.string.text_error_plate))
        }
    }

}