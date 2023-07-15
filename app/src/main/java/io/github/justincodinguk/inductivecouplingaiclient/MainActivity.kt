package io.github.justincodinguk.inductivecouplingaiclient

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.justincodinguk.inductivecouplingaiclient.datastore.dataStore
import io.github.justincodinguk.inductivecouplingaiclient.ui.BotResponseFields
import io.github.justincodinguk.inductivecouplingaiclient.ui.MicButton
import io.github.justincodinguk.inductivecouplingaiclient.ui.UrlAlertDialog
import io.github.justincodinguk.inductivecouplingaiclient.ui.state.MicState
import io.github.justincodinguk.inductivecouplingaiclient.ui.theme.InductiveCouplingAIClientTheme
import io.github.justincodinguk.inductivecouplingaiclient.viewmodel.AiClientViewModel
import io.github.justincodinguk.inductivecouplingaiclient.viewmodel.AiViewModelFactory
import java.util.Locale


class MainActivity : ComponentActivity() {

    private lateinit var textToSpeech: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToSpeech = TextToSpeech(applicationContext) {}
        textToSpeech.language = Locale.UK
        setContent {
            InductiveCouplingAIClientTheme {
                ClientApp()
            }
        }
    }

    @Composable
    fun ClientApp() {

        val viewModel: AiClientViewModel = viewModel(factory = AiViewModelFactory(dataStore))

        val micButtonState by viewModel.micState.collectAsState()
        val responseState by viewModel.responseState.collectAsState()
        val apiUrlState by viewModel.apiUrlState.collectAsState()

        var userQuestion by remember { mutableStateOf("") }
        var shownDialog by remember { mutableStateOf(false) }
        var editMode by remember { mutableStateOf(false) }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                112
            )
        }

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            Locale.getDefault()
        )

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(p0: Float) {}
            override fun onBufferReceived(p0: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(p0: Int) {}
            override fun onPartialResults(p0: Bundle?) {}
            override fun onEvent(p0: Int, p1: Bundle?) {}

            override fun onResults(p0: Bundle?) {
                val question = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?: throw IllegalStateException("Cannot access speech recognizer")
                userQuestion = question[0]
                viewModel.questionBot(question[0])
            }
        })

        if ((!shownDialog and apiUrlState.isEmpty()) or editMode) {
            UrlAlertDialog(viewModel) {
                editMode = false
                shownDialog = true
            }
        }

        if (apiUrlState.isNotEmpty()) {
            viewModel.setUrl(apiUrlState)
        }

        Column(modifier = Modifier.fillMaxSize()) {

            IconButton(onClick = {
                editMode = true
            },
            modifier = Modifier.weight(1f)) {
                Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = "")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(9f)
            ) {

                MicButton(
                    onClick = {
                        viewModel.enableMic()
                        textToSpeech.stop()
                        speechRecognizer.startListening(recognizerIntent)
                    },
                    state = micButtonState,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = stringResource(
                        id = when (micButtonState) {
                            is MicState.MicLoadingState -> R.string.loading
                            is MicState.MicListeningState -> R.string.listening
                            else -> R.string.speak
                        }
                    ),
                    fontSize = 24.sp
                )

                BotResponseFields(
                    userQuestion = userQuestion,
                    botAnswer = responseState.response,
                )


                if (micButtonState is MicState.MicLoadingState) {
                    speechRecognizer.stopListening()
                }

                if(responseState.response.isNotEmpty() and (micButtonState is MicState.MicDisabledState)) {
                    textToSpeech.speak(responseState.response,TextToSpeech.QUEUE_FLUSH,null,null);
                }
            }
        }

    }

    @Composable
    @Preview(showSystemUi = true, showBackground = true)
    fun ClientPreview() {
        InductiveCouplingAIClientTheme {
            ClientApp()
        }
    }
}