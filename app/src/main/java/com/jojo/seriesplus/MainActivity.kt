package com.jojo.seriesplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.jojo.seriesplus.ui.SeriesPlus
import com.jojo.seriesplus.ui.theme.SeriesPlusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeriesPlusTheme {
                val winSizeClass = calculateWindowSizeClass(activity = this).widthSizeClass

                SeriesPlus(winSizeClass)
            }
        }
    }
}