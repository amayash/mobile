package com.example.myapplication.database.entities.composeui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.database.entities.model.CinemaWithSessions
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun SessionList(
    cinemaWithSessions: CinemaWithSessions,
    navController: NavController,
    viewModel: SessionListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    LazyColumn {
        if (cinemaWithSessions.sessions.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.Session_empty_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            items(cinemaWithSessions.sessions, key = { it.uid }) { session ->
                val route = Screen.SessionEdit.route.replace(
                    "{id}", session.uid.toString()
                ).replace(
                    "{cinemaId}", cinemaWithSessions.cinema.uid.toString()
                )
                val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                val formattedDate = dateFormatter.format(session.dateTime)
                Column {
                    Text(
                        text = formattedDate,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Box(modifier = Modifier
                        .padding(vertical = 7.dp)
                        .clickable {
                            navController.navigate(route)
                        }
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        )) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (cinemaWithSessions.cinema.image != null) Image(
                                bitmap = BitmapFactory.decodeByteArray(
                                    cinemaWithSessions.cinema.image,
                                    0,
                                    cinemaWithSessions.cinema.image.size
                                ).asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Цена: ${session.price}\n" + "Билетов: ${session.availableCount}",
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }

                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.addSessionInCart(sessionId = session.uid)
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.deleteSession(session = session)
                                        navController.popBackStack()
                                        navController
                                            .navigate(Screen.CinemaView.route
                                                .replace("{id}",
                                                    cinemaWithSessions.cinema.uid.toString()))
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
