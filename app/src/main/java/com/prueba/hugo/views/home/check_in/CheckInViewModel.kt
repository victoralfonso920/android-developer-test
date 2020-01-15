package com.prueba.hugo.views.home.check_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mobilesv.mor.db.DtaVehicleEntity
import com.prueba.hugo.App
import com.prueba.hugo.R
import com.prueba.hugo.databinding.ItemRecyclerHomeBinding
import com.prueba.hugo.model.Option
import com.prueba.hugo.model.OptionsModel
import com.prueba.hugo.repository.DataRepository
import com.prueba.hugo.tools.*
import kotlinx.android.synthetic.main.item_recycler_home.view.*

/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class CheckInViewModel(private val repository: DataRepository): ViewModel() {

    var imageRes = R.drawable.car_perfil
    var typeUser =  MutableLiveData<String>()
    var optionsData = MutableLiveData<OptionsModel>()
    var updateData = MutableLiveData<Int>()
    var adapter: DynamicBindingAdapter<Option>? = null
    var gotoback = SingleLiveEvent<Boolean>()
    var showRegister = SingleLiveEvent<Boolean>()


    var textIdCar = MutableLiveData<String>()
    var textTime= MutableLiveData<String>()
    var textError= MutableLiveData<String>()
    val editTextContent = Observer<String>{onTextChanged(it)}
    val editTextTime = Observer<String>{onTextChanged(it)}


    fun getDataInit(){
        textTime.value = optionsData.value!!.options?.get(0)!!.name!!.BillParking()
        typeUser.value = optionsData.value!!.options?.get(0)!!.name
    }

    fun generateOptions(){
        launchAPIRequest{
            try {
                launch {
                    optionsData.postValue(repository.getOptions())
                }
            }catch (e:Exception){}
        }
    }

    fun getAdapterOptions(): DynamicBindingAdapter<Option> {

        var pos = 0
        try {
            adapter = DynamicBindingAdapter(
                R.layout.item_recycler_home,
                optionsData.value!!.options!!,
                fun(vh, view, op, p) {
                    if (vh.itemView.isSelected) {
                        pos = p
                    }
                    if(pos == p){
                        view.root.btnOptions.changeState(view.root.context,true)
                    }else{
                        view.root.btnOptions.changeState(view.root.context)
                    }

                    view.root.btnOptions.setOnClickListener {
                        adapter?.let {
                            it.notifyItemChanged(pos)
                            pos = p
                            it.notifyItemChanged(pos)
                        }
                        textTime.postValue(op.name!!.BillParking())
                        typeUser.postValue(op.name)
                    }
                    view as ItemRecyclerHomeBinding
                    view.option = op
                })
        } catch (e: Exception) {
            println(e.message)
        }
        return adapter!!
    }

    fun onTextChanged(it: String) {

    }

    fun registerUser(){
        if(textIdCar.value!!.isNotEmpty()){
            createReguster()
        }else{
            textError.postValue(App.context.getString(R.string.text_error_plate))
        }
    }

    fun activeParking(){
        if(textIdCar.value!!.isNotEmpty()){
            UpdateTime()
        }else{
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

    fun Gotoback(){
        gotoback.postValue(true)
    }

    fun createReguster(){
        val data =   repository.getDataCarsId(textIdCar.value!!)
        if(data.isEmpty()){
            val dtaVehicle = DtaVehicleEntity()
            dtaVehicle.idCar = textIdCar.value
            dtaVehicle.user = typeUser.value
            dtaVehicle.time_start = 0
            dtaVehicle.time_end = 0
            try {
                repository.createData(dtaVehicle)
            }catch (e:Exception){}
            generateDialog()
        }else{
            textError.postValue("Ya existe vehiculo asociado a la placa")
        }

    }

    fun generateDialog(){
        if(repository.getDataCars().size > 0){
            textError.postValue("Se ha registrado el vehiculo")
        }else{
            textError.postValue("Error al registrar el vehiculo")
        }
    }

     fun UpdateTime(){
        val data =   repository.getDataCarsId(textIdCar.value!!)
         if(data.isNotEmpty()){
             val dtaVehicle = DtaVehicleEntity()
             dtaVehicle.idCar = data[0]!!.idCar
             dtaVehicle.user =  data[0]!!.user
             dtaVehicle.time_start = System.currentTimeMillis()
             try {
                 repository.createData(dtaVehicle)
             }catch (e:Exception){}
             generateDialog()
         }else{
             textError.postValue("No existe vehiculo asociado a la placa")
         }

     }

}