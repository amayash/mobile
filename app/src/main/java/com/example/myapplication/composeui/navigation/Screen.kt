package com.example.myapplication.composeui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
    SessionList(
        "Session-list", R.string.Sessions_title,
    ),
    Cart(
        "cart", R.string.Cart_title, Icons.Filled.ShoppingCart
    ),
    OrderList(
    "Order-list", R.string.Order_title, Icons.Filled.List
    ),
    CinemaView(
        "Cinema-view/{id}", R.string.Cinema_view_title, showInBottomBar = false
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
            SessionList,
            Cart,
            OrderList
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}