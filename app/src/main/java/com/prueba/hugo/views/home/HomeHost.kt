package com.prueba.hugo.views.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prueba.hugo.R
import com.prueba.hugo.tools.marginUpdate
import com.prueba.hugo.tools.setTransparentStatusBar
import kotlinx.android.synthetic.main.activity_home_host.*

class HomeHost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_host)
        updateUI()
    }

    private fun updateUI(){
        setTransparentStatusBar()
        home_host.marginUpdate()
    }
}
