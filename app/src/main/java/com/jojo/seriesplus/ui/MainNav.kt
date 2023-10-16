package com.jojo.seriesplus.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jojo.seriesplus.ui.actor.ActorScreen
import com.jojo.seriesplus.ui.details.DetailsScreen
import com.jojo.seriesplus.ui.history.HistoryScreen
import com.jojo.seriesplus.ui.home.HomeScreen
import com.jojo.seriesplus.ui.search.SearchScreen
import com.jojo.seriesplus.util.Routes

@Composable
fun MainNav(
    navController: NavHostController,
    scaffoldPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = Modifier.padding(scaffoldPadding)
    ) {
        composable(Routes.Home.route) {
            HomeScreen(navController)
        }

        composable(
            "${Routes.Search.route}?query={query}",
            arguments = listOf(navArgument("query") { nullable = true })
        ) {
            SearchScreen(navController)
        }

        composable(
            "${Routes.Details.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            DetailsScreen(navController)
        }

        composable(
            "${Routes.Actor.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            ActorScreen(navController)
        }

        composable(Routes.History.route) {
            HistoryScreen(navController)
        }
    }
}