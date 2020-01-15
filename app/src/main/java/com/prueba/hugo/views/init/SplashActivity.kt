package com.prueba.hugo.views.init

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.prueba.hugo.R
import com.prueba.hugo.databinding.ActivitySplashBinding
import com.prueba.hugo.tools.launchActivity
import com.prueba.hugo.views.home.HomeHost
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

            initDataBinding()
            observerLaunch()
    }

    private fun initDataBinding(){
        DataBindingUtil.setContentView<ActivitySplashBinding>(
            this,
            R.layout.activity_splash
        ).apply {
            lifecycleOwner = this@SplashActivity
            splashVm = viewModel
        }
    }

    private fun observerLaunch(){
        viewModel.gotoHome.observe(this, Observer {
            if(it){
                try{
                    launchActivity<HomeHost>(true)
                }catch (e:Exception){
                    println("error ${e.message}")
                    println("error ${e.cause}")
                }
            }
        })
    }

}
