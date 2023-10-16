package com.jojo.seriesplus.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContentBox(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    content: @Composable () -> Unit
) {
    Surface(
        color = color,
        modifier = Modifier
            .padding(12.dp)
            .then(modifier),
        shape = RoundedCornerShape(15.dp)
    ) {
        content()
    }
}