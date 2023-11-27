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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.database.entities.model.Cinema
import kotlinx.coroutines.launch

@Composable
fun CinemaList(
    navController: NavController,
    viewModel: CinemaListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val cinemaPagingItems = viewModel.cinemaListUiState.collectAsLazyPagingItems()

    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.CinemaEdit.route.replace("{id}", 0.toString())
                    navController.navigate(route)
                },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Добавить",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        CinemaList(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            pagingCinema = cinemaPagingItems,
            onClick = { uid: Int ->
                val route = Screen.CinemaView.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onDeleteClick = { cinema: Cinema ->
                coroutineScope.launch {
                    viewModel.deleteCinema(cinema)
                }
            },
            onEditClick = { uid: Int ->
                val route = Screen.CinemaEdit.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
        )
    }
}

@Composable
private fun CinemaList(
    modifier: Modifier = Modifier,
    pagingCinema: LazyPagingItems<Cinema>,
    onClick: (uid: Int) -> Unit,
    onDeleteClick: (cinema: Cinema) -> Unit,
    onEditClick: (cinema: Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
        ) {
            items(pagingCinema.itemCount) { index ->
                val cinema = pagingCinema[index]
                if (cinema != null) {
                    CinemaListItem(
                        cinema = cinema,
                        modifier = Modifier
                            .padding(vertical = 7.dp)
                            .clickable { onClick(cinema.uid) }
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun CinemaListItem(
    cinema: Cinema,
    modifier: Modifier = Modifier,
    onDeleteClick: (cinema: Cinema) -> Unit,
    onEditClick: (cinema: Int) -> Unit
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

            // Добавляем пустое пространство для разделения текста и кнопки
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { onEditClick(cinema.uid) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            }
            IconButton(
                onClick = { onDeleteClick(cinema) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    }
}