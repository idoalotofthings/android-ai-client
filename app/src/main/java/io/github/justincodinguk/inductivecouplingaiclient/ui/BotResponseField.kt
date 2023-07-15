package io.github.justincodinguk.inductivecouplingaiclient.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.justincodinguk.inductivecouplingaiclient.R

@Composable
fun BotResponseFields(
    userQuestion: String,
    botAnswer: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text(
            text = stringResource(R.string.you).format(userQuestion),
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = stringResource(R.string.bot).format(botAnswer),
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        
    }

}