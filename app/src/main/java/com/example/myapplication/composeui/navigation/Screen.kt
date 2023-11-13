package com.example.myapplication.composeui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector = Icons.Filled.Favorite,
    val showInBottomBar: Boolean = true
) {
    CinemaList(
        "Cinema-list", R.string.Cinema_main_title, Icons.Filled.Home
    ),
    CinemaEdit(
        "Cinema-edit/{id}", R.string.Cinema_view_title, showInBottomBar = false
    ),
    SessionEdit(
        "Session-edit/{id}/{cinemaId}", R.string.Session_view_title, showInBottomBar = false
    ),
    CinemaView(
        "Cinema-view/{id}", R.string.Cinema_view_title, showInBottomBar = false
    ),
    SessionList(
        "Session-list", R.string.Sessions_title, showInBottomBar = false
    ),
    Cart(
        "cart", R.string.Cart_title, Icons.Filled.ShoppingCart
    ),
    OrderList(
        "Order-list", R.string.Order_title, Icons.Filled.List
    ),
    OrderView(
        "Order-view/{id}", R.string.Order_view_title, showInBottomBar = false
    ),
    UserProfile(
        "User-profile", R.string.Profile_title, showInBottomBar = false
    );

    companion object {
        val bottomBarItems = listOf(
            CinemaList,
            Cart,
            OrderList
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}