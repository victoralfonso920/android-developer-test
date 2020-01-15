package com.prueba.hugo.views.home.check_out

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.prueba.hugo.R
import com.prueba.hugo.databinding.FragmentCheckOutBinding
import com.prueba.hugo.tools.Cons
import com.prueba.hugo.tools.GotoBack
import com.prueba.hugo.tools.GotoNavigate
import com.prueba.hugo.tools.showAlert
import kotlinx.android.synthetic.main.fragment_check_out.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */

class CheckOutFragment : Fragment() {

    private val viewModel by viewModel<CheckOutViewModel>()
    private var dashBinding: FragmentCheckOutBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return dashBinding?.let {
            it.root
        } ?: FragmentCheckOutBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = this@CheckOutFragment
            chkVm = viewModel
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        observerError()
        observerBack()
        observerPay()
    }

    private fun initData(){
        arguments?.let {
            val id = it.getString(Cons.PLACA,"")
            viewModel.getDataCar(id)
        }
    }


    private fun observerError(){
        viewModel.textError.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity!!.showAlert(it)
            }
        })
    }

    private fun observerBack(){
        viewModel.gotoback.observe(viewLifecycleOwner,Observer{
            if(it){
                pb.GotoBack()
            }
        })
    }

    private fun observerPay(){
        viewModel.gotoPay.observe(viewLifecycleOwner, Observer {
            it?.let {
                val bundle = Bundle()
                bundle.putString(Cons.PLACA,it)
                pb.GotoNavigate(R.id.outt_to_payBillFragment,bundle)
            }
        })
    }


}

