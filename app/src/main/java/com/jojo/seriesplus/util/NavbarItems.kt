package com.jojo.seriesplus.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.jojo.seriesplus.R

enum class NavBarItems(
    @StringRes val stringName: Int,
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    HOME(R.string.nav_home, Routes.Home.route, Icons.Default.Home, Icons.Filled.Home),
    SEARCH(R.string.nav_search, Routes.Search.route, Icons.Default.Search, Icons.Filled.Search),
    HISTORY(R.string.nav_history, Routes.History.route, Icons.Default.History, Icons.Filled.History)
}