package com.jojo.seriesplus.util

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Search : Routes("search")
    object Details : Routes("details")
    object Actor : Routes("actor")
    object History : Routes("history")
}
