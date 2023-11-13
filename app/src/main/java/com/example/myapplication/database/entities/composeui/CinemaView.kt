package com.example.myapplication.database.entities.composeui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCinema
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun CinemaView(
    navController: NavController,
    viewModel: CinemaViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val cinemaUiState by viewModel.cinemaUiState.collectAsState()

    CinemaView(
        cinemaUiState = cinemaUiState,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        onClick = { uid: Int ->
            val route = Screen.SessionEdit.route.replace("{id}", uid.toString())
                .replace("{cinemaId}", cinemaUiState.cinema?.uid.toString())
            navController.navigate(route)
        },
        onClickEdit = { uid: Int ->
            val route = Screen.CinemaEdit.route.replace("{id}", uid.toString())
            navController.navigate(route)
        },
        onClickSession = { uid: Int ->
            val route = Screen.SessionEdit.route.replace("{id}", uid.toString())
                .replace("{cinemaId}", cinemaUiState.cinema?.uid.toString())
            navController.navigate(route)
        },
        onAddToCart = { session: Int ->
            coroutineScope.launch {
                viewModel.addSessionInCart(sessionId = session)
            }
        },
        onDeleteSession = { session: Session ->
            coroutineScope.launch {
                viewModel.deleteSessionInCinema(session = session)
            }
        }
    )
}

@Composable
private fun CinemaView(
    cinemaUiState: CinemaUiState,
    modifier: Modifier = Modifier,
    onClick: (uid: Int) -> Unit,
    onClickEdit: (uid: Int) -> Unit,
    onClickSession: (uid: Int) -> Unit,
    onAddToCart: (sessionId: Int) -> Unit,
    onDeleteSession: (session: Session) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        val cinema: Cinema? = cinemaUiState.cinema
        if (cinema != null)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(color = MaterialTheme.colorScheme.secondary),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "${cinema.name}, ${cinema.year}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                ),
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                            )

                            IconButton(
                                onClick = {
                                    onClickEdit(cinema.uid)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Редактировать",
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }

                        if (cinema.image != null)
                            Image(
                                bitmap = BitmapFactory.decodeByteArray(
                                    cinema.image,
                                    0,
                                    cinema.image.size
                                ).asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(4.dp)
                            )

                        Text(
                            text = cinema.description,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Сеансы",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .weight(1f) // Занимает доступное пространство
                        .padding(top = 8.dp, bottom = 8.dp)
                )

                IconButton(
                    onClick = {
                        onClick(0)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Добавить сеанс",
                    )
                }
            }
        }
        if (cinemaUiState.sessions.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.Session_empty_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            items(
                items = cinemaUiState.sessions,
                key = { it.uid }) { session ->

                if (cinema != null) {
                    SessionListItem(
                        onAddToCart = onAddToCart,
                        session = session,
                        cinema = cinema,
                        modifier = Modifier
                            .padding(vertical = 7.dp)
                            .clickable { onClickSession(session.uid) }
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        onDeleteSession = onDeleteSession)
                }
            }
        }
    }
}

@Composable
private fun SessionListItem(
    onAddToCart: (sessionId: Int) -> Unit,
    onDeleteSession: (session: Session) -> Unit,
    session: SessionFromCinema,
    cinema: Cinema,
    modifier: Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val formattedDate = dateFormatter.format(session.dateTime)
    Column {
        Text(
            text = formattedDate,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Box(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (cinema.image != null)
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(
                            cinema.image,
                            0,
                            cinema.image.size
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
                        text = "Цена: ${session.price}\n" +
                                "Билетов: ${session.availableCount}",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(
                    onClick = { onAddToCart(session.uid) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                // Отрицательное значение для уменьшения расстояния
                Spacer(modifier = Modifier.width(-30.dp))
                IconButton(
                    onClick = {
                        onDeleteSession(
                            Session(
                                uid = session.uid,
                                dateTime = session.dateTime,
                                price = session.price,
                                maxCount = 0,
                                cinemaId = cinema.uid
                            )
                        )
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
