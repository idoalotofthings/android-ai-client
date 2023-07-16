package io.github.justincodinguk.inductivecouplingaiclient.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.justincodinguk.inductivecouplingaiclient.R
import io.github.justincodinguk.inductivecouplingaiclient.viewmodel.AiClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlAlertDialog(viewModel: AiClientViewModel, onSubmit: () -> Unit) {

    AlertDialog(onDismissRequest = {}) {
        Surface {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var urlFieldValue by remember {
                    mutableStateOf(TextFieldValue(""))
                }

                Text(text = stringResource(id = R.string.url), modifier = Modifier.padding(8.dp))

                TextField(
                    value = urlFieldValue,
                    onValueChange = {
                        urlFieldValue = it
                    },
                    modifier = Modifier.padding(8.dp),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.setUrl(urlFieldValue.text)
                            onSubmit()
                        }
                    ),
                    singleLine = true
                )

                Button(
                    onClick = {
                        viewModel.setUrl(urlFieldValue.text)
                        onSubmit()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(stringResource(id = R.string.save))
                }
            }
        }
    }
}