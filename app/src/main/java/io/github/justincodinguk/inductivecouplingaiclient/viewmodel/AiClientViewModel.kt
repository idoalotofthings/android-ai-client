package io.github.justincodinguk.inductivecouplingaiclient.viewmodel

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.justincodinguk.inductivecouplingaiclient.model.Question
import io.github.justincodinguk.inductivecouplingaiclient.repository.AiRepository
import io.github.justincodinguk.inductivecouplingaiclient.ui.state.MicState
import io.github.justincodinguk.inductivecouplingaiclient.ui.state.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import io.github.justincodinguk.inductivecouplingaiclient.repository.ApiUrlRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AiClientViewModel(dataStore: DataStore<Preferences>) : ViewModel() {

    private lateinit var aiRepository: AiRepository
    private val apiUrlRepository = ApiUrlRepository(dataStore)

    private val _micState = MutableStateFlow<MicState>(MicState.MicDisabledState)
    val micState = _micState.asStateFlow()

    private val _responseState = MutableStateFlow(ResponseState(""))
    val responseState = _responseState.asStateFlow()

    val apiUrlState = apiUrlRepository.apiUrlPrefsFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    fun setUrl(url: String) {
        aiRepository = AiRepository(url)
        viewModelScope.launch {
            apiUrlRepository.updateApiUrl(url)
        }
    }

    fun questionBot(question: String) {

        if(!::aiRepository.isInitialized) {
            throw IllegalStateException("URL not set")
        }

        _micState.value = MicState.MicLoadingState

        viewModelScope.launch {
            val answer = aiRepository.questionModel(Question(question)).answer
            _micState.value = MicState.MicDisabledState
            _responseState.update { currentState ->
                currentState.copy(response = answer)
            }
        }
    }

    fun enableMic() {
        _micState.value = MicState.MicListeningState
    }
}

class AiViewModelFactory(private val dataStore: DataStore<Preferences>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AiClientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  AiClientViewModel(dataStore) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}