package com.example.myapplication.entities.composeui

import android.content.res.Configuration
import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.model.Cinema
import com.example.myapplication.entities.model.SessionFromCinema
import com.example.myapplication.ui.theme.PmudemoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun CinemaView(id: Int) {
    val context = LocalContext.current
    val cinemaWithSessions = remember {
        mutableStateListOf<Pair<Cinema, List<SessionFromCinema>>>()
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            cinemaWithSessions.clear()
            cinemaWithSessions
                .addAll(AppDatabase.getInstance(context).cinemaDao().getByUid(id).map { (cinema, sessionFromCinema) ->
                    Pair(cinema, sessionFromCinema)
            })
        }
    }
    val cinema = cinemaWithSessions.firstOrNull()?.first
    val sessions = cinemaWithSessions.firstOrNull()?.second

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                        text = "${cinema?.name ?: ""}, ${cinema?.year ?: 1930}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    if (cinema?.image != null)
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
                        text = cinema?.description ?: "",
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

        if (sessions != null) {
            items(sessions) { session ->
                val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                val formattedDate = dateFormatter.format(session.dateTime)
                Text(
                    text = formattedDate,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (cinema?.image != null)
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
        }
    }
}


@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CinemaViewPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CinemaView(id = 0)
        }
    }
}