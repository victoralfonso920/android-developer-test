package com.prueba.hugo.di.inject_module

import com.prueba.hugo.di.inject_repository.InjectRepository
import com.prueba.hugo.repository.DataRepository
import com.prueba.hugo.views.home.check_in.CheckInViewModel
import com.prueba.hugo.views.home.check_out.CheckOutViewModel
import com.prueba.hugo.views.home.dashboard.DashBoardViewModel
import com.prueba.hugo.views.home.pay_bill.PayBillViewModel
import com.prueba.hugo.views.init.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/* Created by
 Victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
object InjectModule {

    val injectModule: Module = module {
        single { InjectRepository().provideGson() }
        single { DataRepository(get()) }
        viewModel { SplashViewModel() }
        viewModel { DashBoardViewModel(get()) }
        viewModel { CheckOutViewModel(get()) }
        viewModel { CheckInViewModel(get()) }
        viewModel { PayBillViewModel(get()) }
    }
}