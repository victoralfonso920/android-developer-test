package com.prueba.hugo.views.home.dashboard

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prueba.hugo.R
import com.prueba.hugo.databinding.ItemCardHomeBinding
import com.prueba.hugo.model.Option
import com.prueba.hugo.model.OptionsModel
import com.prueba.hugo.repository.DataRepository
import com.prueba.hugo.tools.*

/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class DashBoardViewModel(private val repository: DataRepository): ViewModel() {

    var optionsData = SingleLiveEvent<OptionsModel>()
    var updateData = MutableLiveData<Int>()
    var adapter: DynamicBindingAdapter<Option>? = null

    var imageRes = R.drawable.mercedez



    fun generateOptions(){
        launchAPIRequest{
            try {
                optionsData.postValue(repository.getMenuOptions())
            }catch (e:Exception){}
        }

    }

    fun getAdapterOptions(): DynamicBindingAdapter<Option> {
        try {
            adapter = DynamicBindingAdapter(
                R.layout.item_card_home,
                optionsData.value!!.options!!,
                fun(vh, view, op, p) {
                    view as ItemCardHomeBinding
                    view.menu = op
                    vh.itemView.setSafeOnClickListener {
                        val bundle = Bundle()

                        when(op.name){
                            Cons.INGRESO-> {
                                bundle.putBoolean(Cons.INGRESO,false)
                                view.root.GotoNavigate(R.id.to_checkInFragment,bundle)
                            }
                            Cons.REGISTRO->{
                                bundle.putBoolean(Cons.REGISTRO,true)
                                view.root.GotoNavigate(R.id.to_checkInFragment,bundle)
                            }
                            Cons.SALIDA->{
                                view.root.GotoNavigate(R.id.to_checkOutFragment,bundle)
                            }
                            Cons.PAGOS-> {
                                view.root.GotoNavigate(R.id.to_payBillFragment,bundle)
                            }
                        }
                    }
                })
        } catch (e: Exception) {
            println(e.message)
        }
        return adapter!!
    }

    fun getaDataCars(){
       println( repository.getDataCars())
    }




}