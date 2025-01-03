package com.weatherapp.core

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavOptions
import com.weatherapp.R

object TransitionUtils {
    val generalAnimationOptions by lazy {
        NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit).build()
    }

    val slideInTransition =
        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) + fadeIn(
            animationSpec = tween(700)
        )

    val slideOutTransition =
        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) +
                fadeOut(animationSpec = tween(700))


}
