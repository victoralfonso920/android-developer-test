package com.prueba.hugo.tools

import android.view.animation.Animation
import android.view.animation.Transformation

/* Created by
 Victor Hernandez
 on 14/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class ProgressBarAnimation (
    private val from: Float,
    private val to: Float,
    private val pb: CustomProgressBar
) :
    Animation() {
    override fun applyTransformation(
        paramFloat: Float,
        paramTransformation: Transformation?
    ) {
        super.applyTransformation(paramFloat, paramTransformation)
        val f = from + paramFloat * (to - from)
        pb.progress = f.toInt()
    }

}