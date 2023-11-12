package com.example.myapplication.database.entities.composeui

import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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

    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.CinemaEdit.route.replace("{id}", 0.toString())
                    navController.navigate(route)
                },
            ) {
                Icon(Icons.Filled.Add, "Добавить")
            }
        }
    ) { innerPadding ->
        CinemaView(
            cinemaUiState = viewModel.cinemaUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onClick = { uid: Int ->
                val route = Screen.CinemaEdit.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onClickSession = { uid: Int ->
                val route = Screen.SessionEdit.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onSwipe = { session: Session ->
                coroutineScope.launch {
                    viewModel.deleteSessionInCinema(session = session)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    dismissState: DismissState,
    session: SessionFromCinema,
    cinema: Cinema,
    onClickSession: (uid: Int) -> Unit
) {
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.EndToStart
        ),
        background = {
            val backgroundColor by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                    else -> MaterialTheme.colorScheme.background
                },
                label = ""
            )
            val iconScale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.5f,
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier.scale(iconScale),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        dismissContent = { SessionListItem(
            session = session,
            cinema = cinema,
            modifier = Modifier
                .padding(vertical = 7.dp)
                .clickable { onClickSession(session.uid) }
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CinemaView(
    cinemaUiState: CinemaUiState,
    modifier: Modifier = Modifier,
    onClick: (uid: Int) -> Unit,
    onClickSession: (uid: Int) -> Unit,
    onSwipe: (session: Session) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        val cinema = cinemaUiState.cinemaDetails
        if (cinema.uid > 0)
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
                        Text(
                            text = "${cinema.name}, ${cinema.year}",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
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
            Text(
                text = "Сеансы",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
            )
        }
        if (cinemaUiState.cinemaDetails.sessions.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.Cinema_empty_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            items(
                items = cinema.sessions,
                key = { it.uid.toString() }) { session ->
                val dismissState: DismissState = rememberDismissState(
                    positionalThreshold = { 200.dp.toPx() }
                )

                if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                    onSwipe(
                        Session(
                            uid = session.uid,
                            dateTime = session.dateTime,
                            price = session.price,
                            maxCount = session.availableCount,
                            cinemaId = cinemaUiState.cinemaDetails.toCinema().uid
                        )
                    )
                }

                SwipeToDelete(
                    dismissState = dismissState,
                    session = session,
                    cinema = cinemaUiState.cinemaDetails.toCinema(cinemaUiState.cinemaDetails.uid),
                    onClickSession = onClickSession,
                )
            }
        }
    }

}

@Composable
private fun SessionListItem(
    session: SessionFromCinema, cinema: Cinema, modifier: Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val formattedDate = dateFormatter.format(session.dateTime)
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

        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .size(24.dp)
                .clickable {}
                .align(Alignment.CenterEnd),
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}