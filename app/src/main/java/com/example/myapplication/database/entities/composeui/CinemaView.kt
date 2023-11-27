package com.example.myapplication.database.entities.composeui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.database.entities.model.Cinema

@Composable
fun CinemaView(
    navController: NavController,
    viewModel: CinemaViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val cinemaUiState = viewModel.cinemaUiState

    LaunchedEffect(Unit) {
        viewModel.refreshState()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        val cinema: Cinema? = cinemaUiState.cinemaWithSessions?.cinema
        if (cinema != null) {
            Box(
                modifier = Modifier
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
                    val route = Screen.SessionEdit.route.replace("{id}", 0.toString())
                        .replace(
                            "{cinemaId}",
                            cinemaUiState.cinemaWithSessions?.cinema?.uid.toString()
                        )
                    navController.navigate(route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Добавить сеанс",
                )
            }
        }
        if (cinemaUiState.cinemaWithSessions != null) {
            SessionList(viewModel, navController)
        }
    }
}
