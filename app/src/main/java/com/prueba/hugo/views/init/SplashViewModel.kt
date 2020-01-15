package com.prueba.hugo.views.init

import androidx.lifecycle.ViewModel
import com.prueba.hugo.tools.SingleLiveEvent

/* Created by
 Victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class SplashViewModel: ViewModel() {

    var gotoHome = SingleLiveEvent<Boolean>()


    fun onclickSplash(){
        gotoHome.postValue(true)
    }
}