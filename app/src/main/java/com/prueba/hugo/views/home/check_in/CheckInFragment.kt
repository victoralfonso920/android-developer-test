package com.prueba.hugo.views.home.check_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.prueba.hugo.databinding.FragmentCheckInBinding
import com.prueba.hugo.model.Option
import com.prueba.hugo.tools.*
import kotlinx.android.synthetic.main.fragment_check_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */

class CheckInFragment : Fragment() {

    private val viewModel by viewModel<CheckInViewModel>()
    private var checkInBinding: FragmentCheckInBinding? = null
    private var optionsAdapter: DynamicBindingAdapter<Option>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return checkInBinding?.let {
            it.root
        } ?: FragmentCheckInBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = this@CheckInFragment
            chkinVm = viewModel
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOptions()
        observerOptions()
        observerUpdate()
        observerError()
        observerBack()
        initData()
    }

    private fun initData(){
        arguments?.let {
            val show = it.getBoolean(Cons.REGISTRO,false)
            viewModel.showRegister.postValue(show)
            println(it.getBoolean(Cons.REGISTRO,false))
        }
    }


    private fun getOptions() {
        viewModel.generateOptions()
    }

    private fun observerOptions() {
        viewModel.optionsData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.options!!.isNotEmpty()){
                    viewModel.getDataInit()
                    updateListOptions()
                }
            }
        })
    }

    private fun observerUpdate() {
        viewModel.updateData.observe(viewLifecycleOwner, Observer {
            rcOptions.adapter?.let { adp ->
                adp.notifyItemChanged(it)
            }
        })
    }

    private fun observerError(){
        viewModel.textError.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity!!.showAlert(it)
            }
        })
    }

    private fun updateListOptions() {
        optionsAdapter = viewModel.getAdapterOptions()
        optionsAdapter?.let { dy ->
            rcOptions?.configureRecyclerBinding(dy, false)
            rcOptions.setHasFixedSize(false)
            rcOptions.isNestedScrollingEnabled = false
            dy.notifyDataSetChanged()
        }

    }

    private fun observerBack(){
        viewModel.gotoback.observe(viewLifecycleOwner,Observer{
            if(it){
                rcOptions.GotoBack()
            }
        })
    }

}

