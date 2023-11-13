package com.example.myapplication.composeui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.composeui.Cart
import com.example.myapplication.database.entities.composeui.CinemaList
import com.example.myapplication.database.entities.composeui.CinemaView
import com.example.myapplication.database.entities.composeui.OrderList
import com.example.myapplication.database.entities.composeui.OrderView
import com.example.myapplication.datastore.DataStoreManager
import com.example.myapplication.database.entities.composeui.UserProfile
import com.example.myapplication.database.entities.composeui.edit.CinemaEdit
import com.example.myapplication.database.entities.composeui.edit.SessionEdit

@Composable
fun Topbar(
    navController: NavHostController,
    currentScreen: Screen?
) {
    var searchQuery by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (
                navController.previousBackStackEntry != null
                && (currentScreen == null || !currentScreen.showInBottomBar)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigateUp() },
                    tint = MaterialTheme.colorScheme.secondary
                )
            } else
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigate(Screen.UserProfile.route) },
                    tint = MaterialTheme.colorScheme.secondary
                )

            Spacer(modifier = Modifier.width(16.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = { newValue -> searchQuery = newValue },
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        RoundedCornerShape(18.dp)
                    )
                    .padding(start = 13.dp, top = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { }
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { },
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun Navbar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.primary) {
        Screen.bottomBarItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun Navhost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    isDarkTheme: MutableState<Boolean>,
    dataStore: DataStoreManager,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController,
        startDestination = Screen.CinemaList.route,
        modifier.padding(innerPadding)
    ) {
        composable(Screen.CinemaList.route) { CinemaList(navController) }
        composable(Screen.OrderList.route) { OrderList(navController, 1) }
        composable(Screen.Cart.route) { Cart() }
        composable(Screen.UserProfile.route) { UserProfile(isDarkTheme, dataStore) }
        composable(
            Screen.CinemaEdit.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            CinemaEdit(navController)
        }
        composable(
            Screen.SessionEdit.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType },
                navArgument("cinemaId") { type = NavType.IntType })
        ) {
            SessionEdit(navController)
        }
        composable(
            Screen.CinemaView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { CinemaView(navController) }
        }
        composable(
            Screen.OrderView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { OrderView(it.getInt("id")) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavbar(
    isDarkTheme: MutableState<Boolean>,
    dataStoreManager: DataStoreManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = currentDestination?.route?.let { Screen.getItem(it) }

    Scaffold(
        topBar = {
            Topbar(navController, currentScreen)
        },
        bottomBar = {
            if (currentScreen == null || currentScreen.showInBottomBar) {
                Navbar(navController, currentDestination)
            }
        }
    ) { innerPadding ->
        Navhost(navController, innerPadding, isDarkTheme, dataStoreManager)
    }
}
