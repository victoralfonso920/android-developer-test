package com.prueba.hugo.views.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilesv.mor.db.DtaVehicleEntity
import com.prueba.hugo.databinding.FragmentDashboardBinding
import com.prueba.hugo.model.Option
import com.prueba.hugo.tools.DynamicAdapterRealm
import com.prueba.hugo.tools.DynamicBindingAdapter
import com.prueba.hugo.tools.configureRecyclerBinding
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/* Created by
 victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */

class DashBoardFragment : Fragment() {

    private val viewModel by viewModel<DashBoardViewModel>()
    private var dashBinding: FragmentDashboardBinding? = null
    private var optionsAdapter: DynamicBindingAdapter<Option>? = null
    private var carAdapter: DynamicAdapterRealm<DtaVehicleEntity>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return dashBinding?.let {
            it.root
        } ?: FragmentDashboardBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = this@DashBoardFragment
            dsVm = viewModel
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOptions()
        observerOptions()
        observerUpdate()

    }


    private fun getOptions() {
        viewModel.generateOptions()
        viewModel.getaDataCars()
    }

    private fun observerOptions() {
        viewModel.optionsData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.options!!.isNotEmpty()){
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

    private fun updateListOptions() {
        optionsAdapter = viewModel.getAdapterOptions()
        optionsAdapter?.let { dy ->
            rcMenu?.configureRecyclerBinding(dy,false,true)
            rcMenu.setHasFixedSize(false)
            rcMenu.isNestedScrollingEnabled = false
            dy.notifyDataSetChanged()
        }

    }




}

