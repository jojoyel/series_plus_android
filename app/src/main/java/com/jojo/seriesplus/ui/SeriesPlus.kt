package com.jojo.seriesplus.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jojo.seriesplus.SeriesPlus
import com.jojo.seriesplus.util.NavBarItems

@Composable
fun SeriesPlus(windowSizeClass: WindowWidthSizeClass) {

    val navController = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { BottomBar(navController = navController) }
    ) { padding ->
        MainNav(navController = navController, scaffoldPadding = padding)
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val items = NavBarItems.values()

    NavigationBar {
        val navBarStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBarStackEntry?.destination

        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                if (it.route != null) it.route!!.startsWith(item.route) else it.route == item.route
            } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                alwaysShowLabel = items.size <= 4,
                label = { Text(text = stringResource(item.stringName)) },
                icon = {
                    Icon(
                        if (selected) item.selectedIcon else item.icon,
                        contentDescription = null
                    )
                })
        }
    }
}