package com.genaku.reduce

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.genaku.reduce.theme.AppTheme

@Preview
@Composable
fun TrafficPreview() {
    AppTheme {
        Traffic(TrafficScreenModel())
    }
}
