package com.sema.spotififi.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sema.spotififi.R

/**
 * A composable that represents the default loading animation for the app.
 * @param isVisible indicates whether the loading animation is visible.
 * @param modifier the modifier to be applied to  [AnimatedVisibility]
 * used in this composable.
 */
@Composable
fun DefaultLoadingAnimation(
    isVisible: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val loadingAnimationComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            R.raw.lottie_loading_anim
        )
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5))
                .background(MaterialTheme.colors.surface)
                .padding(32.dp),
            contentAlignment = Alignment.Center

        ) {
            LottieAnimation(
                modifier = Modifier.fillMaxHeight(),
                composition = loadingAnimationComposition,
                iterations = LottieConstants.IterateForever
            )
        }
    }
}