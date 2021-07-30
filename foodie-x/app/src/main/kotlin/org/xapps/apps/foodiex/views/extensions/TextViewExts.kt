package org.xapps.apps.foodiex.views.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.widget.TextView
import org.xapps.apps.foodiex.R


fun TextView.setFadeInText(text: String) {
    if (tag == null || tag == false) {
        tag = true
        animate()
            .alpha(0.0f)
            .setDuration(resources.getInteger(R.integer.animation_duration_fastest).toLong())
            .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                setText(text)

                animate()
                    .alpha(1.0f)
                    .setDuration(resources.getInteger(R.integer.animation_duration_fastest).toLong())
                    .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        tag = false
                    }
                })
            }
        })
    }
}