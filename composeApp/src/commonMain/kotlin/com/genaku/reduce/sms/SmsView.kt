package com.genaku.reduce.sms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun SmsView(screenModel: SmsScreenModel, navigator: Navigator) {
    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp)) {
        val smsState = screenModel.smsReducer.state.collectAsState()
        if (smsState.value == SmsState.Exit) navigator.pop()
        val loadingState = screenModel.loadingState.collectAsState()
        val errorState = screenModel.errorState.collectAsState()
        var inputText by rememberSaveable { mutableStateOf("") }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputText,
            onValueChange = {
                inputText = it
            },
        )
        Row {
            Button(
                modifier = Modifier.padding(8.dp),
                enabled = loadingState.value == LoadingState.Idle && inputText.isNotBlank(),
                onClick = {
                    screenModel.smsReducer.checkSms(inputText)
                }
            ) {
                Text("Send")
            }
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    screenModel.smsReducer.cancel()
                }
            ) {
                Text(if (loadingState.value == LoadingState.Idle) "Exit" else "Cancel")
            }
        }
        val errorValue = errorState.value
        when {
            loadingState.value == LoadingState.Active -> Text("Processing...")

            errorValue is ErrorState.Error -> Text(
                color = Color.Red,
                text = errorValue.error.msg
            )

            smsState.value == SmsState.SmsConfirmed -> Text(
                fontWeight = FontWeight.Bold,
                text = "Success!"
            )
        }
    }
}