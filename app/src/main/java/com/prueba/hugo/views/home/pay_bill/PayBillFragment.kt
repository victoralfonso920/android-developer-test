package com.prueba.hugo.views.home.pay_bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.prueba.hugo.R
import com.prueba.hugo.databinding.FragmentPayBillBinding
import com.prueba.hugo.tools.*
import kotlinx.android.synthetic.main.fragment_pay_bill.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */

class PayBillFragment : Fragment() {

    private val viewModel by viewModel<PayBillViewModel>()
    private var dashBinding: FragmentPayBillBinding? = null

    private var id:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return dashBinding?.let {
            it.root
        } ?: FragmentPayBillBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = this@PayBillFragment
            payVm = viewModel
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerBack()
        observerError()
        initData()
        observerSucces()
    }

    private fun initData(){
        arguments?.let {
             id = it.getString(Cons.PLACA,"")
            viewModel.checkUsaerData(id)
        }
        if(id.isEmpty()){
            etIdCarPay.makeVisibility(true)
            textCalcula.makeVisibility(true)
        }
    }

    private fun observerBack(){
        viewModel.gotoback.observe(viewLifecycleOwner, Observer{
            if(it){
                btnPayParking.GotoBack()
            }
        })
    }

    private fun observerError(){
        viewModel.textError.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity!!.showAlert(it){
                    it.GotoBack()
                }
            }
        })
    }

    private fun observerSucces(){
        viewModel.textSuccess.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity!!.showAlert(it){
                    it.setOnClickListener {
                        btnPayParking.GotoSpecificFragmentClear(R.id.dashBoardFragment)
                    }
                }
            }
        })
    }



}

