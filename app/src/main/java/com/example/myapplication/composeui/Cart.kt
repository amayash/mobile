package com.example.myapplication.composeui

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.database.entities.composeui.AppViewModelProvider
import com.example.myapplication.database.entities.composeui.CartUiState
import com.example.myapplication.database.entities.composeui.CartViewModel
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.ui.theme.PmudemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun Cart(
    viewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val cartUiState = viewModel.cartUiState

    LaunchedEffect(Unit) {
        viewModel.refreshState()
    }

    Cart(
        cartUiState = cartUiState,
        modifier = Modifier
            .padding(all = 10.dp),
        onSwipe = { session: SessionFromCart, user: Int ->
            coroutineScope.launch {
                viewModel.removeFromCart(
                    session = Session(
                        uid = session.uid,
                        dateTime = session.dateTime,
                        price = session.price,
                        maxCount = 0,
                        cinemaId = session.cinemaId
                    ), user = user
                )
            }
        },
        onChangeCount = { session: SessionFromCart, user: Int, count: Int ->
            coroutineScope.launch {
                viewModel.updateFromCart(
                    session = Session(
                        uid = session.uid,
                        dateTime = session.dateTime,
                        price = session.price,
                        maxCount = 0,
                        cinemaId = session.cinemaId
                    ), userId = user, count = count, availableCount = session.availableCount
                )
            }
        },
        onAddToOrder = { sessions: List<SessionFromCart>, user: Int ->
            coroutineScope.launch {
                viewModel.addToOrder(sessions = sessions, userId = user)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Cart(
    cartUiState: CartUiState,
    modifier: Modifier,
    onSwipe: (SessionFromCart, Int) -> Unit,
    onChangeCount: (SessionFromCart, Int, Int) -> Unit,
    onAddToOrder: (List<SessionFromCart>, Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(cartUiState.sessionList, key = { it.uid.toString() }) { session ->
            val dismissState: DismissState = rememberDismissState(
                positionalThreshold = { 200.dp.toPx() }
            )

            if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                onSwipe(session, 1)
            }

            SwipeToDelete(
                dismissState = dismissState,
                session = session,
                onChangeCount = onChangeCount
            )
        }
    }
    Column {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onAddToOrder(cartUiState.sessionList, 1) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) { Text("Купить") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    dismissState: DismissState,
    session: SessionFromCart,
    onChangeCount: (SessionFromCart, Int, Int) -> Unit,
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
        dismissContent = {
            SessionListItem(
                session = session,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                onChangeCount = onChangeCount
            )
        }
    )
}

@Composable
private fun SessionListItem(
    session: SessionFromCart,
    modifier: Modifier = Modifier,
    onChangeCount: (SessionFromCart, Int, Int) -> Unit,
) {
    var currentCount by remember { mutableStateOf(session.count) }

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
                if (session.cinema.image != null)
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(
                            session.cinema.image,
                            0,
                            session.cinema.image.size
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
                        text = "${session.cinema.name}, ${session.cinema.year}\n" +
                                "Цена: ${session.price}\n" +
                                "${currentCount}/${session.availableCount}",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(10.dp)
                        ) // Задаем фон для кнопок
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onChangeCount(session, 1, --currentCount) }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.minus),
                                contentDescription = "Уменьшить",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(10.dp)
                            )
                        }

                        Text(
                            text = "$currentCount",
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        IconButton(
                            onClick = {
                                onChangeCount(
                                    session,
                                    1,
                                    if (currentCount != session.availableCount) ++currentCount else currentCount
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Увеличить",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Cart()
        }
    }
}
