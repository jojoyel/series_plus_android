package com.jojo.seriesplus.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TextIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    contentDescription: String?
) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.then(modifier)) {
        Icon(icon, contentDescription)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text)
    }
}