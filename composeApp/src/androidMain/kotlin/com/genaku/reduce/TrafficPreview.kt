package com.genaku.reduce

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.genaku.reduce.theme.AppTheme
import com.genaku.reduce.traffic.TrafficView
import com.genaku.reduce.traffic.TrafficScreenModel

@Preview
@Composable
fun TrafficPreview() {
    AppTheme {
        TrafficView(TrafficScreenModel())
    }
}
