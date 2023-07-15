package io.github.justincodinguk.inductivecouplingaiclient.ui

import io.github.justincodinguk.inductivecouplingaiclient.ui.animation.Pulsating
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.justincodinguk.inductivecouplingaiclient.R
import io.github.justincodinguk.inductivecouplingaiclient.ui.animation.Rotating
import io.github.justincodinguk.inductivecouplingaiclient.ui.state.MicState
import io.github.justincodinguk.inductivecouplingaiclient.ui.theme.InductiveCouplingAIClientTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MicButton(
    onClick: () -> Unit,
    state: MicState,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .width(250.dp)
            .height(250.dp)
            .padding(16.dp)
    ) {

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                scaleIn(tween(500)) with scaleOut(tween(500))
            }
        ) { state ->
            when (state) {
                is MicState.MicListeningState -> Pulsating {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_listening
                        ),
                        contentDescription = stringResource(id = R.string.speak),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }

                is MicState.MicDisabledState -> {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_mic
                        ),
                        contentDescription = stringResource(id = R.string.speak),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }

                else -> Rotating {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_loading),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }
            }
        }
    }
}


@Composable
@Preview
fun MicButtonPreview() {
    InductiveCouplingAIClientTheme {

    }
}