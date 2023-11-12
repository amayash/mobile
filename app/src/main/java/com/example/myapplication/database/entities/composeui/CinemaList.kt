package com.example.myapplication.database.entities.composeui

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.ui.theme.PmudemoTheme
import kotlinx.coroutines.launch

@Composable
fun CinemaList(
    navController: NavController,
    viewModel: CinemaListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val cinemaListUiState by viewModel.cinemaListUiState.collectAsState()

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
        CinemaList(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            cinemaList = cinemaListUiState.cinemaList,
            onClick = { uid: Int ->
                val route = Screen.CinemaView.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onSwipe = { cinema: Cinema ->
                coroutineScope.launch {
                    viewModel.deleteCinema(cinema)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    dismissState: DismissState,
    cinema: Cinema,
    onClick: (uid: Int) -> Unit
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
            CinemaListItem(cinema = cinema,
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .clickable { onClick(cinema.uid) }
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp)
                    ))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CinemaList(
    modifier: Modifier = Modifier,
    cinemaList: List<Cinema>,
    onClick: (uid: Int) -> Unit,
    onSwipe: (cinema: Cinema) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (cinemaList.isEmpty()) {
            Text(
                text = stringResource(R.string.Cinema_empty_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp)
            ) {
                items(items = cinemaList, key = { it.uid.toString() }) { cinema ->
                    val dismissState: DismissState = rememberDismissState(
                        positionalThreshold = { 200.dp.toPx() }
                    )

                    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                        onSwipe(cinema)
                    }

                    SwipeToDelete(
                        dismissState = dismissState,
                        cinema = cinema,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CinemaListItem(
    cinema: Cinema, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            Text(
                "${cinema.name}, ${cinema.year}",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CinemaEmptyListPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CinemaList(
                cinemaList = listOf(),
                onClick = {},
                onSwipe = {}
            )
        }
    }
}