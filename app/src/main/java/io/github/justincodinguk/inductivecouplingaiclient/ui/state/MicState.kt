package io.github.justincodinguk.inductivecouplingaiclient.ui.state

sealed class MicState {
    object MicDisabledState : MicState()

    object MicListeningState : MicState()

    object MicLoadingState : MicState()
}